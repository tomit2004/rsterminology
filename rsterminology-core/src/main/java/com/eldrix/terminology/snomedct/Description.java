package com.eldrix.terminology.snomedct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.cayenne.Cayenne;

import com.eldrix.terminology.snomedct.auto._Description;

public class Description extends _Description {
	private static final long serialVersionUID = 1L; 

	public enum Status {
		CURRENT(0, "Current", true),
		NON_CURRENT(1, "Retired", false),
		DUPLICATE(2, "Duplicate", false),
		OUTDATED(3, "Outdated", false),
		ERRONEOUS(5, "Erroneous", false),
		LIMITED(6, "Limited", true),
		INAPPROPRIATE(7, "Inappropriate", false),
		CONCEPT_NON_CURRENT(8, "Concept non-current", false),
		MOVED_ELSEWHERE(10, "Moved elsewhere", false),
		PENDING_MOVE(11, "Pending move", true);

		private static final Map<Integer, Status> _lookup = new HashMap<Integer, Status>();
		private static List<Integer> activeCodes = new ArrayList<>();
		static {
			for (Status st : Status.values()) {
				_lookup.put(st.code, st);
				if (st.isActive()) {
					activeCodes.add(st.code);
				}
			}
		};

		int code;
		String title;
		boolean active;
		Status(int code, String title, boolean active) {
			this.code = code;
			this.title = title;
			this.active = active;
		}

		public String getTitle() {
			return this.title;
		}
		public boolean isActive() {
			return this.active;
		}
		public static Optional<Status> getStatus(int code) {
			return Optional.ofNullable(_lookup.get(code));
		}
		public static List<Integer> activeCodes() {
			return activeCodes;
		}
	}

	public enum Type {
		UNSPECIFIED(0, "Unspecified"),
		PREFERRED(1, "Preferred"),
		SYNONYM(2, "Synonym"),
		FULLY_SPECIFIED_NAME(3, "Fully specified name");
		public final int code;
		String name;
		Type(int code, String name) {
			this.code = code;
			this.name = name;
		}
		private static final Map<Integer, Type> _lookup = new HashMap<Integer, Type>();
		static {
			for (Type t : values()) {
				_lookup.put(t.code, t);
			}
		};
		public static Optional<Type> getType(int code) {
			return Optional.ofNullable(_lookup.get(code));
		}
	}



	@Override
	public String toString() {
		return Cayenne.longPKForObject(this) + "-" + getTerm();
	};
	
	/**
	 * Return the status of this description.
	 * @return
	 */
	public Optional<Status> getStatus() {
		return Status.getStatus(getDescriptionStatusCode());
	}
	
	/**
	 * Return the type of description.
	 * @return
	 */
	public Optional<Type> getType() {
		return Type.getType(getDescriptionTypeCode());
	}

	/**
	 * Is this description a preferred description for the associated concept?
	 * @return
	 */
	public boolean isPreferred() {
		return this.getDescriptionTypeCode().equals(Type.PREFERRED.code);
	}
	
	/**
	 * Is this description current / active?
	 * @return
	 */
	public boolean isActive() {
		return this.getStatus().orElse(Status.ERRONEOUS).active;
	}

}
