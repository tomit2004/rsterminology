package com.eldrix.terminology.snomedct.auto;

import java.util.Date;
import java.util.List;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.exp.Property;

import com.eldrix.terminology.snomedct.Concept;
import com.eldrix.terminology.snomedct.Project;

/**
 * Class _Project was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Project extends CayenneDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<String> ADDRESS1 = new Property<String>("address1");
    public static final Property<String> ADDRESS2 = new Property<String>("address2");
    public static final Property<String> ADDRESS3 = new Property<String>("address3");
    public static final Property<String> ADDRESS4 = new Property<String>("address4");
    public static final Property<String> ADVERTISE_TO_ALL = new Property<String>("advertiseToAll");
    public static final Property<String> CAN_OWN_EQUIPMENT = new Property<String>("canOwnEquipment");
    public static final Property<String> CARE_PLAN_INFORMATION = new Property<String>("carePlanInformation");
    public static final Property<Date> DATE_FROM = new Property<Date>("dateFrom");
    public static final Property<Date> DATE_TO = new Property<Date>("dateTo");
    public static final Property<String> ETHICS = new Property<String>("ethics");
    public static final Property<String> EXCLUSION_CRITERIA = new Property<String>("exclusionCriteria");
    public static final Property<Integer> ID = new Property<Integer>("id");
    public static final Property<String> INCLUSION_CRITERIA = new Property<String>("inclusionCriteria");
    public static final Property<String> IS_PRIVATE = new Property<String>("isPrivate");
    public static final Property<Integer> LOGO_ATTACHMENT_FK = new Property<Integer>("logoAttachmentFk");
    public static final Property<String> LONG_DESCRIPTION = new Property<String>("longDescription");
    public static final Property<String> NAME = new Property<String>("name");
    public static final Property<String> POSTCODE = new Property<String>("postcode");
    public static final Property<Long> SPECIALTY_CONCEPT_FK = new Property<Long>("specialtyConceptFk");
    public static final Property<String> TITLE = new Property<String>("title");
    public static final Property<String> TYPE = new Property<String>("type");
    public static final Property<String> VIRTUAL = new Property<String>("virtual");
    public static final Property<List<Project>> CHILDREN = new Property<List<Project>>("children");
    public static final Property<List<Concept>> COMMON_CONCEPTS = new Property<List<Concept>>("commonConcepts");
    public static final Property<Project> PARENT = new Property<Project>("parent");

    public void setAddress1(String address1) {
        writeProperty("address1", address1);
    }
    public String getAddress1() {
        return (String)readProperty("address1");
    }

    public void setAddress2(String address2) {
        writeProperty("address2", address2);
    }
    public String getAddress2() {
        return (String)readProperty("address2");
    }

    public void setAddress3(String address3) {
        writeProperty("address3", address3);
    }
    public String getAddress3() {
        return (String)readProperty("address3");
    }

    public void setAddress4(String address4) {
        writeProperty("address4", address4);
    }
    public String getAddress4() {
        return (String)readProperty("address4");
    }

    public void setAdvertiseToAll(String advertiseToAll) {
        writeProperty("advertiseToAll", advertiseToAll);
    }
    public String getAdvertiseToAll() {
        return (String)readProperty("advertiseToAll");
    }

    public void setCanOwnEquipment(String canOwnEquipment) {
        writeProperty("canOwnEquipment", canOwnEquipment);
    }
    public String getCanOwnEquipment() {
        return (String)readProperty("canOwnEquipment");
    }

    public void setCarePlanInformation(String carePlanInformation) {
        writeProperty("carePlanInformation", carePlanInformation);
    }
    public String getCarePlanInformation() {
        return (String)readProperty("carePlanInformation");
    }

    public void setDateFrom(Date dateFrom) {
        writeProperty("dateFrom", dateFrom);
    }
    public Date getDateFrom() {
        return (Date)readProperty("dateFrom");
    }

    public void setDateTo(Date dateTo) {
        writeProperty("dateTo", dateTo);
    }
    public Date getDateTo() {
        return (Date)readProperty("dateTo");
    }

    public void setEthics(String ethics) {
        writeProperty("ethics", ethics);
    }
    public String getEthics() {
        return (String)readProperty("ethics");
    }

    public void setExclusionCriteria(String exclusionCriteria) {
        writeProperty("exclusionCriteria", exclusionCriteria);
    }
    public String getExclusionCriteria() {
        return (String)readProperty("exclusionCriteria");
    }

    public void setId(int id) {
        writeProperty("id", id);
    }
    public int getId() {
        Object value = readProperty("id");
        return (value != null) ? (Integer) value : 0;
    }

    public void setInclusionCriteria(String inclusionCriteria) {
        writeProperty("inclusionCriteria", inclusionCriteria);
    }
    public String getInclusionCriteria() {
        return (String)readProperty("inclusionCriteria");
    }

    public void setIsPrivate(String isPrivate) {
        writeProperty("isPrivate", isPrivate);
    }
    public String getIsPrivate() {
        return (String)readProperty("isPrivate");
    }

    public void setLogoAttachmentFk(Integer logoAttachmentFk) {
        writeProperty("logoAttachmentFk", logoAttachmentFk);
    }
    public Integer getLogoAttachmentFk() {
        return (Integer)readProperty("logoAttachmentFk");
    }

    public void setLongDescription(String longDescription) {
        writeProperty("longDescription", longDescription);
    }
    public String getLongDescription() {
        return (String)readProperty("longDescription");
    }

    public void setName(String name) {
        writeProperty("name", name);
    }
    public String getName() {
        return (String)readProperty("name");
    }

    public void setPostcode(String postcode) {
        writeProperty("postcode", postcode);
    }
    public String getPostcode() {
        return (String)readProperty("postcode");
    }

    public void setSpecialtyConceptFk(Long specialtyConceptFk) {
        writeProperty("specialtyConceptFk", specialtyConceptFk);
    }
    public Long getSpecialtyConceptFk() {
        return (Long)readProperty("specialtyConceptFk");
    }

    public void setTitle(String title) {
        writeProperty("title", title);
    }
    public String getTitle() {
        return (String)readProperty("title");
    }

    public void setType(String type) {
        writeProperty("type", type);
    }
    public String getType() {
        return (String)readProperty("type");
    }

    public void setVirtual(String virtual) {
        writeProperty("virtual", virtual);
    }
    public String getVirtual() {
        return (String)readProperty("virtual");
    }

    public void addToChildren(Project obj) {
        addToManyTarget("children", obj, true);
    }
    public void removeFromChildren(Project obj) {
        removeToManyTarget("children", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<Project> getChildren() {
        return (List<Project>)readProperty("children");
    }


    public void addToCommonConcepts(Concept obj) {
        addToManyTarget("commonConcepts", obj, true);
    }
    public void removeFromCommonConcepts(Concept obj) {
        removeToManyTarget("commonConcepts", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<Concept> getCommonConcepts() {
        return (List<Concept>)readProperty("commonConcepts");
    }


    public void setParent(Project parent) {
        setToOneTarget("parent", parent, true);
    }

    public Project getParent() {
        return (Project)readProperty("parent");
    }


}