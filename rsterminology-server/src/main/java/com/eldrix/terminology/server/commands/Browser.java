package com.eldrix.terminology.server.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.query.ObjectSelect;

import com.eldrix.terminology.snomedct.Concept;
import com.eldrix.terminology.snomedct.Search;
import com.eldrix.terminology.snomedct.Search.ResultItem;
import com.eldrix.terminology.snomedct.semantic.Amp;
import com.eldrix.terminology.snomedct.semantic.Ampp;
import com.eldrix.terminology.snomedct.semantic.Dmd;
import com.eldrix.terminology.snomedct.semantic.Dmd.Product;
import com.eldrix.terminology.snomedct.semantic.Tf;
import com.eldrix.terminology.snomedct.semantic.Vmp;
import com.eldrix.terminology.snomedct.semantic.Vmpp;
import com.eldrix.terminology.snomedct.semantic.Vtm;
import com.google.inject.Inject;
import com.google.inject.Provider;

import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.meta.application.CommandMetadata;

/**
 * This is a very simply command-line SNOMED-CT browser.
 * It is extremely rudimentary but it is intended to be help quickly find a concept and browse the hierarchy.
 * @author Mark Wardle
 *
 */
public class Browser extends CommandWithMetadata {

	@Inject
	public Provider<ServerRuntime> cayenne;

	private Concept _currentConcept;

	private static CommandMetadata createMetadata() {
		return CommandMetadata.builder(Browser.class)
				.description("Browse and search SNOMED-CT interactively.")
				.build();
	}

	public Browser() {
		super(createMetadata());
	}

	@Override
	public CommandOutcome run(Cli cli) {
		System.out.println("SNOMED-CT interactive browser and search.");
		boolean quit = false;
		while (!quit) {
			if (currentConcept() != null) {
				System.out.println("****************************************");
				System.out.println("Current: "+currentConcept().getConceptId()  + ": " + currentConcept().getFullySpecifiedName());
				System.out.println("****************************************");
			}
			System.out.println("Enter command:");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			try {
				String line = bufferedReader.readLine();
				quit = performCommand(line.trim());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return CommandOutcome.succeeded();
	}

	private boolean performCommand(String line) {
		if (performQuit(line) == true) {
			return true;
		}
		performHelp(line);
		performShowConcept(line);
		performShowDescriptions(line);
		performShowChildRelationships(line);
		performShowRecursiveChildRelationships(line);
		performFind(line);
		return false;
	}
	private boolean performQuit(String line) {
		return "q".equalsIgnoreCase(line) || "quit".equalsIgnoreCase(line);
	}

	private void performHelp(String line) {
		if ("h".equalsIgnoreCase(line) || "?".equalsIgnoreCase(line) || "help".equalsIgnoreCase(line)) {
			System.out.println("help/h/? : This help");
			System.out.println("quit/q   : Quit");
			System.out.println("s <conceptId> : Show or change the currently selected concept");
			System.out.println("d        : Show descriptions for currently selected concept");
			System.out.println("c        : Show child relationships for currently selected concept");
			System.out.println("cc       : Show recursive children (IS-A) for currently selected concept");
			System.out.println("f <name> : Find a concept matching the specified name");
			System.out.println("dmd      : Displays DMD information for currently selected concept");
		}
	}

	private void setCurrentConcept(Concept c) {
		_currentConcept = c;
	}
	private Concept currentConcept() {
		return _currentConcept;
	}

	private void performShowConcept(String line) {
		Matcher m = Pattern.compile("s (\\d*)").matcher(line);
		if (m.matches()) {
			try {
				long conceptId = Long.parseLong(m.group(1));
				ObjectContext context = cayenne.get().newContext();
				Concept c = ObjectSelect.query(Concept.class, Concept.CONCEPT_ID.eq(conceptId)).selectOne(context);
				if (c != null) {
					setCurrentConcept(c);
					printConcept(c, false);
				}
				else {
					System.err.println("No concept found with identifier: "+conceptId);
				}
			}
			catch (NumberFormatException e) {
				System.err.println("Invalid concept identifier");
			}
		}
		if ("s".equalsIgnoreCase(line.trim()) && currentConcept() != null) {
			printConcept(currentConcept(), true);
		}
	}

	private void performShowDescriptions(String line) {
		if ("d".equalsIgnoreCase(line) && currentConcept() != null) {
			System.out.println("Descriptions (synonyms):");
			currentConcept().getDescriptions().stream().forEach(d -> {
				System.out.println("  |-- " + d.getDescriptionId() + ": " + d.getTerm());
			});
		}
	}

	private void performShowChildRelationships(String line) {
		if ("c".equalsIgnoreCase(line.trim()) && currentConcept() != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("Child relationships:");
			currentConcept().getChildRelationships().stream()
				.sorted( (r1, r2) -> r1.getRelationshipTypeConcept().getFullySpecifiedName().compareTo(r2.getRelationshipTypeConcept().getFullySpecifiedName()))
				.forEach(r -> {
					sb.append("\n  |    |-" + r.getSourceConcept().getFullySpecifiedName()  + " " +r.getSourceConceptId() + " "+ " [" + r.getRelationshipTypeConcept().getFullySpecifiedName() + "] " + r.getTargetConcept().getFullySpecifiedName());
			});
			System.out.println(sb.toString());
		}
	}

	private void performShowRecursiveChildRelationships(String line) {
		if ("cc".equalsIgnoreCase(line.trim()) && currentConcept() != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("Recursive children:");
			currentConcept().getRecursiveChildConcepts().stream()
				.forEach(c -> {
					sb.append("\n  |    |-" + c.getFullySpecifiedName()  + " " +c.getConceptId());
			});
			System.out.println(sb.toString());
		}
	}

	
	private static void printConcept(Concept c, boolean includeRelations) {
		StringBuilder sb = new StringBuilder();
		sb.append("Concept : " + c.getConceptId());
		sb.append(":[");
		sb.append(c.getFullySpecifiedName());
		sb.append("]:");
		sb.append(c.getPreferredDescription().getTerm());
		if (Dmd.Product.productForConcept(c) != null) {
			sb.append("  DM&D concept: " + Dmd.Product.productForConcept(c));
		}
		if (includeRelations) {
			sb.append("\n  |-Recursive parents:");
			c.getRecursiveParentConcepts().forEach(parent ->
			sb.append("\n  |    |-" + parent.getConceptId() + " " + parent.getFullySpecifiedName()));
			sb.append("\n  |-Parent relationships:");
			c.getParentRelationships().forEach(r -> {
				sb.append("\n  |    |-" + r.getSourceConcept().getFullySpecifiedName() + " [" + r.getRelationshipTypeConcept().getFullySpecifiedName() + "] " + r.getTargetConcept().getFullySpecifiedName() + " " + r.getTargetConceptId());
			});
		}
		System.out.println(sb.toString());
		if (Dmd.Product.productForConcept(c) != null) {
			showDmd(c);
		}
	}

	private void performFind(String line) {
		Matcher m = Pattern.compile("^f(?<number>\\d*)?\\s+(\\[(?<roots>.*?)\\])?\\s?(?<search>.*)").matcher(line);
		if (m.matches()) {
			String number = m.group("number");		// number of results requested
			String roots = m.group("roots");		// root identifiers
			String search = m.group("search");
			try {
				int maxHits = 20;
				if (number != null && number.length() > 0) {
					try {
						int h = Integer.parseInt(number);
						maxHits = h;
					}
					catch (NumberFormatException e) {
						;
					}
				}
				long[] rootConceptIds = new long[] { 138875005 };
				if (roots != null && roots.length() > 0) {
					long[] r = Search.parseLongArray(roots);
					if (r.length > 0) {
						rootConceptIds = r;
					}
				}
				List<ResultItem> results = Search.getInstance().newBuilder().search(search).setMaxHits(maxHits+1).withRecursiveParent(rootConceptIds).build().search();
				int count = results.size();
				if (count > 0) {
					results.subList(0, count > maxHits ? maxHits : count).forEach(ri -> {
						System.out.println(ri.getTerm() + " -- " + ri.getPreferredTerm() + " -- " + ri.getConceptId());
					});
					if (results.size() > maxHits) {
						System.out.println("Warning: more results available than shown!");
					}
				}
				else {
					System.out.println("No results found");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static void showDmd(Concept c) {
		Optional<Product> product = Product.productForConcept(c);
		if (product.isPresent()) {
			switch (product.get()) {
			case VIRTUAL_THERAPEUTIC_MOIETY:
				showVtm(0, new Vtm(c), true);
				break;
			case ACTUAL_MEDICINAL_PRODUCT:
				showAmp(0, new Amp(c), true);
				break;
			case ACTUAL_MEDICINAL_PRODUCT_PACK:
				showAmpp(new Ampp(c));
				break;
			case TRADE_FAMILY:
				showTf(0, new Tf(c), true);
				break;
			case VIRTUAL_MEDICINAL_PRODUCT:
				showVmp(0, new Vmp(c), true);
				break;
			case VIRTUAL_MEDICINAL_PRODUCT_PACK:
				showVmpp(new Vmpp(c));
				break;
			default:
				break;

			}
		} else {
			System.out.println("Not a DMD concept: " +c.getFullySpecifiedName());
		}
	}
	private static void showTf(int indent, Tf tf, boolean showOthers) {
		StringBuilder sb = new StringBuilder();
		sb.append(indent(indent));
		sb.append("TF:");
		appendDmdProduct(sb, tf);
		System.out.println(sb.toString());
		if (showOthers) {
			tf.getDispensedDoseForms().forEach(c -> showConcept(indent+2, "doseForm", c));
			tf.getAmps().forEach(amp -> showAmp(indent+2, amp, false));
			tf.getVmps().forEach(vmp -> showVmp(indent+2, vmp, false));
			tf.getVtms().forEach(vtm -> showVtm(indent+2, vtm, false));
		}
	}
	private static void showVmp(int indent, Vmp vmp, boolean showOthers) {
		StringBuilder sb = new StringBuilder();
		sb.append(indent(indent));
		sb.append("VMP: ");
		appendDmdProduct(sb, vmp);
		sb.append(" Rx:");
		sb.append(vmp.isPrescribable());
		System.out.println(sb.toString());
		if (showOthers) {
			vmp.getAmps().forEach(amp -> showAmp(indent+2, amp, false));
			vmp.getVtms().forEach(vtm -> showVtm(indent+2, vtm, false));
			vmp.getTfs().forEach(tf -> showTf(indent+2, tf, false));
			vmp.getActiveIngredients().forEach(c -> System.out.println(indent(indent+2) + "activeIngredient:" + c.getPreferredDescription().getTerm()));
			vmp.getDispensedDoseForm().ifPresent(c -> showConcept(indent+2, "doseForm", c));
		}
	}
	private static void showVmpp(Vmpp vmpp) {

	}
	private static void showAmp(int indent, Amp amp, boolean showOthers) {
		StringBuilder sb = new StringBuilder();
		sb.append(indent(indent));
		sb.append("AMP:");
		appendDmdProduct(sb, amp);
		if (amp.shouldPrescribeVmp()) {
			sb.append(" ( should prescribe VMP:");
			appendDmdProduct(sb, amp.getVmp().get());
			sb.append(")");
		}
		System.out.println(sb.toString());
	}
	private static void showAmpp(Ampp amp) {

	}
	private static void showVtm(int indent, Vtm vtm, boolean showOthers) {
		StringBuilder sb = new StringBuilder();
		sb.append(indent(indent));
		sb.append("VTM:");
		appendDmdProduct(sb, vtm);
		System.out.println(sb.toString());
		if (showOthers) {
			vtm.getVmps().forEach(vmp -> showVmp(indent + 2, vmp, false));
			vtm.getTfs().forEach(tf -> showTf(indent + 2, tf, false));
			vtm.getDispensedDoseForms().forEach(c -> showConcept(indent+2, "doseForm", c));
		}
	}

	private static void showConcept(int indent, String title, Concept c) {
		System.out.println(indent(indent) + title + ":" + c.getPreferredDescription().getTerm());
	}

	private static String indent(int n) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<n; i++) {
			sb.append('-');
		}
		return sb.toString();
	}
	private static void appendDmdProduct(StringBuilder sb, Dmd product) {
		sb.append(product.getConcept().getConceptId());
		sb.append(":");
		sb.append(product.getConcept().getPreferredDescription().getTerm());
	}
}
