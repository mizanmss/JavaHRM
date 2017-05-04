package com.ctrends.ctrendsee.service.hrm_ed;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ctrends.ctrendsee.bean.Validation;
import com.ctrends.ctrendsee.bean.WSResponse;
import com.ctrends.ctrendsee.dao.hrm_ed.IBranchDAO;
import com.ctrends.ctrendsee.model.grc_ac.User;
import com.ctrends.ctrendsee.model.hrm_ed.Branch;
import com.ctrends.ctrendsee.model.hrm_ed.BranchDetails;
import com.ctrends.ctrendsee.model.hrm_ed.BranchDetailsWip;
import com.ctrends.ctrendsee.model.hrm_ed.BranchWip;
import com.ctrends.ctrendsee.model.tna_wf.Workflow;
import com.ctrends.ctrendsee.model.tna_wf.WorkflowDetails;
import com.ctrends.ctrendsee.model.tna_wf.WorkflowRecords;
import com.ctrends.ctrendsee.service.grc_ac.IUserService;
import com.ctrends.ctrendsee.service.tna_wf.IWorkflowService;

@Service("branchService")
public class BranchService implements IBranchService {

	@Autowired
	IBranchDAO iBranchDAO;

	@Autowired
	IUserService IUserService;

	@Autowired
	IWorkflowService workflowService;

	@Override
	public Map<String, String> insert(Map<String, String[]> request) {
		return null;
	}

	@Override
	public WSResponse insertData(Map<String, String[]> request) {
		System.out.println("::: " + request.get("branch_code")[0]);
		ArrayList<String> errorMessages = this.validateBranch(request);
		if (errorMessages.size() == 0) {
			try {
				Map<String, String> params = new HashMap<String, String>();
				String mode;
				UUID id = null;
				User currentUser = IUserService.getCurrentUser();
				Workflow wf = workflowService.getWorkflow("BRANCH", "CREATE", "ALL", 0,
						request.get("company_code")[0].toUpperCase());
				BranchWip branchWip = new BranchWip();
				branchWip.setCompanyCode(request.get("company_code")[0]);
				System.out.println(request.get("company_code")[0] + "::::::::::::::company_code:::::::::::::::");
				branchWip.setBranchCode(request.get("branch_code")[0]);
				branchWip.setBranchName(request.get("branch_name")[0]);
				branchWip.setAddress(request.get("address")[0]);
				branchWip.setContacts(request.get("contacts")[0]);
				branchWip.setChiefCode(request.get("chief_code")[0]);
				branchWip.setChiefName(request.get("chief_name")[0]);
				branchWip.setChiefUserName(request.get("chief_username")[0]);
				branchWip.setChiefDesig(request.get("chief_desig")[0]);
				branchWip.setChiefMobile(request.get("chief_mobile")[0]);
				branchWip.setChiefEmail(request.get("chief_email")[0]);
				branchWip.setChiefCompanyCode(request.get("chief_company_code")[0]);
				branchWip.setChiefCompanyName(request.get("chief_company_name")[0]);
				branchWip.setRoleCode(request.get("chief_role_code")[0]);
				branchWip.setRoleName(request.get("chief_role_name")[0]);

				/**
				 * start branch location table
				 */

				String[] latitude = (String[]) request.get("latitude[]");
				String[] longitude = (String[]) request.get("longitude[]");
				String[] altitude = (String[]) request.get("altitude[]");
				String[] address = (String[]) request.get("address[]");

				String[] locCod = (String[]) request.get("location_code[]");
				String[] locName = (String[]) request.get("location_name[]");

				List<BranchDetailsWip> branchDetailsWips = new ArrayList<BranchDetailsWip>();
				for (int i = 0; i < locName.length; i++) {
					BranchDetailsWip branchDetailswip = new BranchDetailsWip();
					branchDetailswip.setLatitude(Double.parseDouble(latitude[i]));
					branchDetailswip.setLongitude(Double.parseDouble(longitude[i]));
					branchDetailswip.setAltitude(Double.parseDouble(altitude[i]));
					branchDetailswip.setAddress(address[i]);
					branchDetailswip.setLocationCode(locCod[i]);
					branchDetailswip.setLocationName(locName[i]);

					branchDetailswip.setBranchCode(request.get("branch_code")[0]);
					branchDetailswip.setBranchName(request.get("branch_name")[0]);

					branchDetailswip.setActionTypeCode("CREATE");
					branchDetailswip.setActionTypeName("Create");
					branchDetailswip.setWfStatus("DRAFT");
					;

					// extra information add
					branchDetailswip.setClientCode(currentUser.getClientCode());
					branchDetailswip.setClientName(currentUser.getClientName());
					branchDetailswip.setCompanyCode(currentUser.getCompanyCode());
					branchDetailswip.setCompanyName(currentUser.getCompanyName());
					branchDetailswip.setCreatedByCode(currentUser.getCreatedByCode());
					branchDetailswip.setCreatedByName(currentUser.getCreatedByName());
					branchDetailswip.setCreatedByUsername(currentUser.getCreatedByUsername());
					branchDetailswip.setCreatedByCode(currentUser.getEmpCode());
					branchDetailswip.setCreatedByName(currentUser.getEmpName());
					branchDetailswip.setCreatedByUsername(currentUser.getUsername());
					branchDetailswip.setCreatedByEmail(currentUser.getEmail());
					branchDetailswip.setCreatedByCompanyCode(currentUser.getCompanyCode());
					branchDetailswip.setCreatedByCompanyName(currentUser.getCompanyName());
					branchDetailswip.setCreatedAt(new Timestamp(System.currentTimeMillis()));
					// add list sequensially
					branchDetailsWips.add(i, branchDetailswip);
				}

				branchWip.setClientCode(currentUser.getClientCode());
				branchWip.setClientName(currentUser.getClientName());
				// branchWip.setCompanyCode(currentUser.getCompanyCode());
				// branchWip.setCompanyName(currentUser.getCompanyName());
				branchWip.setCreatedByCode(currentUser.getCreatedByCode());
				branchWip.setCreatedByName(currentUser.getCreatedByName());
				branchWip.setCreatedByUsername(currentUser.getCreatedByUsername());
				branchWip.setCreatedByCode(currentUser.getEmpCode());
				branchWip.setCreatedByName(currentUser.getEmpName());
				branchWip.setCreatedByUsername(currentUser.getUsername());
				branchWip.setCreatedByEmail(currentUser.getEmail());
				branchWip.setCreatedByCompanyCode(currentUser.getCompanyCode());
				branchWip.setCreatedByCompanyName(currentUser.getCompanyName());
				branchWip.setCreatedAt(new Timestamp(System.currentTimeMillis()));

				if (wf == null) {
					List<BranchDetails> brdetList = new ArrayList<>();
					for (int i = 0; i < branchDetailsWips.size(); i++) {
						BranchDetailsWip brdetwip = branchDetailsWips.get(i);
						brdetList.add(i, brdetwip.toBranchDetails());
					}
					branchWip.setSteps(brdetList);
					mode = "doc";
					Branch branch = branchWip.toBranch(); // convert
															// HrRoleWip
					id = iBranchDAO.insertDoc(branch);
					String suuid = id.toString();
					params.put("id", suuid);
					params.put("mode", mode);

				} else if (wf != null) {
					branchWip.setSteps(branchDetailsWips);
					mode = "wip";
					branchWip.setActionTypeCode("CREATE");
					branchWip.setActionTypeName("Create");
					branchWip.setWfStatus("DRAFT");
					id = iBranchDAO.insertWip(branchWip);
					String suuid = id.toString();
					params.put("id", suuid);
					params.put("mode", mode);

				}

				if (id != null) {
					return WSResponse.createSuccess("Saved successfully", UUID.fromString(params.get("id")),
							params.get("mode"), null);
				} else {
					return WSResponse.createFailure("Unsuccessful", (String[]) errorMessages.toArray());
				}
			} catch (Exception e) {
				e.printStackTrace();
				return WSResponse.createFailure("Unsuccessful", (String[]) errorMessages.toArray());
			}
		} else {
			return WSResponse.createFailure("Unsuccessful", errorMessages.toArray(new String[errorMessages.size()]));
		}
	}

	@Override
	public WSResponse updateData(Map<String, String[]> request) {
		System.out.println("::: " + request.get("branch_code")[0]);
		ArrayList<String> errorMessages = this.validateBranch(request);
		if (errorMessages.size() == 0) {
			User currentUser = IUserService.getCurrentUser();
			BranchWip branchWip;
			Branch branch;
			String newMode = request.get("mode")[0];
			UUID id;
			Workflow wf = null;
			boolean wfExists = false;
			UUID uuid = UUID.fromString(request.get("id")[0]);

			if (request.get("mode")[0].equals("wip")) {
				branchWip = iBranchDAO.getWipById(uuid);
			} else {
				wf = workflowService.getWorkflow("BRANCH", request.get("action_type_code")[0], "ALL", 0,
						request.get("company_code")[0].toUpperCase());

				if (wf == null) {
					wfExists = false;
					branch = iBranchDAO.getDocById(uuid);
					branchWip = BranchWip.fromBrach(branch);

				} else {
					newMode = "wip";
					Branch branchOrginal = iBranchDAO.getDocById(uuid);
					branchOrginal.setLocked(Boolean.parseBoolean("true"));
					branchOrginal.setLockedByCode(currentUser.getEmpCode());
					branchOrginal.setLockedByName(currentUser.getEmpName());
					branchOrginal.setLockedByCompanyCode(currentUser.getCompanyCode());
					branchOrginal.setLockedByCompanyName(currentUser.getCompanyName());
					branchOrginal.setLockedByEmail(currentUser.getEmail());
					branchOrginal.setLockedByUsername(currentUser.getUsername());
					branchOrginal.setLockedAt(new Timestamp(System.currentTimeMillis()));

					id = iBranchDAO.updateDoc(branchOrginal);
					System.out.println("::::::::::::::doc:::::::::::::::::::");

					branchWip = new BranchWip();

					branchWip.setWfStatus("DRAFT");
					branchWip.setRefId(branchOrginal.getId());
					branchWip.setRefCode(branchOrginal.getBranchCode());
					branchWip.setCreatedByCode(branchOrginal.getCreatedByCode());
					branchWip.setCreatedByName(branchOrginal.getCreatedByName());
					branchWip.setCreatedByEmail(branchOrginal.getCreatedByEmail());
					branchWip.setCreatedByUsername(branchOrginal.getCreatedByUsername());
					branchWip.setCreatedAt(new Timestamp(System.currentTimeMillis()));
					branchWip.setCreatedByCompanyCode(branchOrginal.getCreatedByCompanyCode());
					branchWip.setCreatedByCompanyName(branchOrginal.getCreatedByCompanyName());

					branchWip.setActionTypeCode(request.get("action_type_code")[0]);
					branchWip.setActionTypeName(request.get("action_type_name")[0]);
				}
			}

			String[] latitude = (String[]) request.get("latitude[]");
			String[] longitude = (String[]) request.get("longitude[]");
			String[] altitude = (String[]) request.get("altitude[]");
			String[] address = (String[]) request.get("address[]");

			String[] locCod = (String[]) request.get("location_code[]");
			String[] locName = (String[]) request.get("location_name[]");

			List<BranchDetailsWip> branchLoctionWips = new ArrayList<>();
			for (int i = 0; i < locCod.length; i++) {
				BranchDetailsWip branchDetailsWip = new BranchDetailsWip();
				branchDetailsWip.setLatitude(Double.parseDouble(latitude[i]));
				branchDetailsWip.setLongitude(Double.parseDouble(longitude[i]));
				branchDetailsWip.setAltitude(Double.parseDouble(altitude[i]));
				branchDetailsWip.setAddress(address[i]);
				branchDetailsWip.setLocationCode(locCod[i]);
				branchDetailsWip.setLocationName(locName[i]);
				branchDetailsWip.setMasterId(uuid);
				branchDetailsWip.setBranchCode(request.get("branch_code")[0]);
				branchDetailsWip.setBranchName(request.get("branch_name")[0]);

				branchDetailsWip.setWfStatus("DRAFT");

				branchDetailsWip.setActionTypeCode(request.get("action_type_code")[0]);
				branchDetailsWip.setActionTypeName(request.get("action_type_name")[0]);

				// extra information add
				branchDetailsWip.setClientCode(currentUser.getClientCode());
				branchDetailsWip.setClientName(currentUser.getClientName());
				branchDetailsWip.setCompanyCode(currentUser.getCompanyCode());
				branchDetailsWip.setCompanyName(currentUser.getCompanyName());
				branchDetailsWip.setCreatedByCode(currentUser.getCreatedByCode());
				branchDetailsWip.setCreatedByName(currentUser.getCreatedByName());
				branchDetailsWip.setCreatedByUsername(currentUser.getCreatedByUsername());
				branchDetailsWip.setCreatedByCode(currentUser.getEmpCode());
				branchDetailsWip.setCreatedByName(currentUser.getEmpName());
				branchDetailsWip.setCreatedByUsername(currentUser.getUsername());
				branchDetailsWip.setCreatedByEmail(currentUser.getEmail());
				branchDetailsWip.setCreatedByCompanyCode(currentUser.getCompanyCode());
				branchDetailsWip.setCreatedByCompanyName(currentUser.getCompanyName());
				branchDetailsWip.setCreatedAt(new Timestamp(System.currentTimeMillis()));
				// add list sequensially
				branchLoctionWips.add(i, branchDetailsWip);

			}

			branchWip.setBranchCode(request.get("branch_code")[0].toUpperCase());
			branchWip.setBranchName(request.get("branch_name")[0]);
			branchWip.setAddress(request.get("address")[0]);
			branchWip.setContacts(request.get("contacts")[0]);
			branchWip.setChiefCode(request.get("chief_code")[0]);
			branchWip.setChiefName(request.get("chief_name")[0]);
			branchWip.setChiefUserName(request.get("chief_username")[0]);
			branchWip.setChiefDesig(request.get("chief_desig")[0]);
			branchWip.setChiefMobile(request.get("chief_mobile")[0]);
			branchWip.setChiefEmail(request.get("chief_email")[0]);
			branchWip.setChiefCompanyCode(request.get("chief_company_code")[0]);
			branchWip.setChiefCompanyName(request.get("chief_company_name")[0]);

			// finally add branch lcotion wip within list

			branchWip.setUpdatedByCode(currentUser.getEmpCode());
			branchWip.setUpdatedByName(currentUser.getEmpName());
			branchWip.setUpdatedByEmail(currentUser.getEmail());
			branchWip.setUpdatedByUsername(currentUser.getUsername());
			branchWip.setUpdatedByCompanyCode(currentUser.getCompanyCode());
			branchWip.setUpdatedByCompanyName(currentUser.getCompanyName());
			branchWip.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

			branchWip.setClientCode(currentUser.getClientCode());
			branchWip.setClientName(currentUser.getClientName());
			branchWip.setCompanyCode(currentUser.getCompanyCode());
			branchWip.setCompanyName(currentUser.getCompanyName());

			Map<String, String> data = new HashMap<String, String>();

			if (!wfExists && newMode.equals("doc")) {
				List<BranchDetails> brdetList = new ArrayList<>();
				for (int i = 0; i < branchLoctionWips.size(); i++) {
					BranchDetailsWip brdetwip = branchLoctionWips.get(i);
					brdetList.add(i, brdetwip.toBranchDetails());
				}
				branchWip.setSteps(brdetList);
				System.out.println("::::::updateDoc::::::");
				id = iBranchDAO.updateDoc(branchWip.toBranch());
				System.out.println(id + "::::::updateDoc::::::");

				if (id != null) {
					return WSResponse.createSuccess("Saved successfully", id, newMode, branchWip.toBranch());
				} else {
					return WSResponse.createFailure("Unsuccessful", (String[]) errorMessages.toArray());
				}
			} else {
				branchWip.setSteps(branchLoctionWips);
				id = iBranchDAO.updateWip(branchWip);

				if (id != null) {
					return WSResponse.createSuccess("Saved successfully", id, newMode, branchWip);
				} else {
					return WSResponse.createFailure("Unsuccessful", (String[]) errorMessages.toArray());
				}
			}

		} else {
			return WSResponse.createFailure("Unsuccessful", errorMessages.toArray(new String[errorMessages.size()]));
		}
	}

	@Override
	public List<BranchWip> getAll() {
		return null;
	}

	@Override
	public List<Branch> getBranchByCompany(String companyCode) {
		return iBranchDAO.getBranchByCompany(companyCode);
	}

	@Override
	public BranchWip getById(UUID id) {
		return null;
	}

	@Override
	public Map<String, String> update(Map<String, String[]> request) {
		return null;
	}

	@Override
	public List<Branch> find(Map<String, String> map) {

		return iBranchDAO.getDocs(map);
	}

	@Override
	public UUID delete(Map<String, String[]> requestMap) {
		String mode = requestMap.get("mode")[0];
		UUID id = UUID.fromString(requestMap.get("id")[0]);
		UUID delId = null;
		if (mode.equals("wip")) {
			delId = iBranchDAO.deleteWip(id);
		}
		return null;
	}

	@Override
	public BranchWip getById(UUID id, String mode) {
		BranchWip branchWip;
		if (mode.equals("wip")) {
			branchWip = iBranchDAO.getWipById(id);
		} else {
			Branch branch = iBranchDAO.getDocById(id);
			branchWip = BranchWip.fromBrach(branch);
		}
		return branchWip;
	}

	@Override
	public List<BranchDetailsWip> getBranchDetailsById(UUID masterId, String mode) {
		List<BranchDetailsWip> branchDetailsWip = new ArrayList<>();
		if (mode.equals("wip")) {
			branchDetailsWip = iBranchDAO.getBranchDetailsWipByID(masterId);
		} else {
			List<BranchDetails> branchLocation = iBranchDAO.getDocBranchDetailsById(masterId);
			int i = 0;
			for (BranchDetails blist : branchLocation) {
				BranchDetailsWip brlocwip = new BranchDetailsWip();
				brlocwip.setLocationCode(blist.getLocationCode());
				brlocwip.setLocationName(blist.getLocationName());
				branchDetailsWip.add(i++, brlocwip);
			}

		}
		System.out.println(branchDetailsWip.size() + "::::::::::::::service:::::::::::");
		return branchDetailsWip;
	}

	@Override
	public UUID delete(UUID id, String mode) {
		if (mode.equals("wip")) {
			return iBranchDAO.deleteWip(id);
		} else {
			return iBranchDAO.deleteDoc(id);
		}
	}

	@Override
	public Map<String, String> submit(Map<String, String[]> request) {
		String outcome = "";
		UUID uuid = UUID.fromString(request.get("id")[0]);
		BranchWip branchWip = iBranchDAO.getWipById(uuid);
		Workflow wf = workflowService.getWorkflow("BRANCH", branchWip.getActionTypeCode(), "ALL", 0,
				branchWip.getCompanyCode());
		User currentUser = IUserService.getCurrentUser();

		branchWip.setWfStatus("SUBMITTED");
		branchWip.setSubmittedByCode(currentUser.getEmpCode());
		branchWip.setSubmittedByName(currentUser.getEmpName());
		branchWip.setSubmittedByUsername(currentUser.getUsername());
		branchWip.setSubmittedByEmail(currentUser.getEmail());
		branchWip.setSubmittedByCompanyCode(currentUser.getCompanyCode());
		branchWip.setSubmittedByCompanyName(currentUser.getCompanyName());

		List<BranchDetailsWip> branchdetailsWips = iBranchDAO.getBranchDetailsWipByID(branchWip.getId());
		System.err.println(branchdetailsWips.size() + "::::::::branchLocationWip::::::service::::::::");
		for (int i = 0; i < branchdetailsWips.size(); i++) {
			branchdetailsWips.get(i).setWfStatus("SUBMITTED");
			branchdetailsWips.get(i).setSubmittedByCode(currentUser.getEmpCode());
			branchdetailsWips.get(i).setSubmittedByName(currentUser.getEmpName());
			branchdetailsWips.get(i).setSubmittedByUsername(currentUser.getUsername());
			branchdetailsWips.get(i).setSubmittedByEmail(currentUser.getEmail());
			branchdetailsWips.get(i).setSubmittedByCompanyCode(currentUser.getCompanyCode());
			branchdetailsWips.get(i).setSubmittedByCompanyName(currentUser.getCompanyName());
		}
		// branch locations list
		branchWip.setSteps(branchdetailsWips);

		iBranchDAO.updateWip(branchWip);
		try {
			WorkflowDetails nextStep = workflowService.processWorkflow(wf, branchWip.toReferenceWip(), "Requested",
					"ACCEPT", null, currentUser.toApprover());
		} catch (Exception e) {
			e.getMessage();
		}

		Map<String, String> data = new HashMap<String, String>();
		String suuid = branchWip.getId().toString();
		data.put("id", suuid);
		data.put("outcome", "success");
		return data;
	}

	@Override
	public Map<String, String> approve(Map<String, String[]> request) {
		System.out.println(request.get("id")[0]);
		UUID uuid = UUID.fromString(request.get("id")[0]);
		BranchWip branchWip = iBranchDAO.getWipById(uuid);
		Branch branch = null;
		String outcome = "";

		if (branchWip == null) {
			// Way to send WSresponse through Controller
			// return "ERROR";

		}

		Workflow wf = workflowService.getWorkflow("BRANCH", branchWip.getActionTypeCode(), "ALL", 0,
				branchWip.getCompanyCode());
		User currentUser = IUserService.getCurrentUser();

		try {
			WorkflowDetails nextStep = workflowService.processWorkflow(wf, branchWip.toReferenceWip(), "Requested",
					"ACCEPT", null, currentUser.toApprover());
			if (nextStep == null) {
				if (branchWip.getActionTypeCode().equals("CREATE")) {
					branch = new Branch();
				} else if (branchWip.getActionTypeCode().equals("EDIT")) {
					System.err.println("::::::::EDIT:::::::::1:::::");
					branch = iBranchDAO.getDocById(branchWip.getRefId());
				}

				branch.setLocked(false);
				branch.setLockedByCode("");
				branch.setLockedByName("");
				branch.setBranchCode(branchWip.getBranchCode());
				branch.setBranchName(branchWip.getBranchName());

				UUID refId;
				try {
					refId = branchWip.getRefId();
				} catch (Exception e) {
					refId = null;
				}

				List<BranchDetailsWip> branchdetailsWips = iBranchDAO
						.getBranchDetailsWipByID((refId == null) ? branchWip.getId() : branchWip.getRefId());
				System.out.println(branchdetailsWips.size() + "::::::::branchLocationWip::::::::::::::");
				List<BranchDetails> branchDetailsList = new ArrayList<>();
				for (int i = 0; i < branchdetailsWips.size(); i++) {
					BranchDetails branchDetails = new BranchDetails();
					branchDetails.setLatitude(branchdetailsWips.get(i).getLatitude());
					branchDetails.setLongitude(branchdetailsWips.get(i).getLongitude());
					branchDetails.setAltitude(branchdetailsWips.get(i).getAltitude());
					branchDetails.setAddress(branchdetailsWips.get(i).getAddress());
					branchDetails.setLocationCode(branchdetailsWips.get(i).getLocationCode());
					branchDetails.setLocationName(branchdetailsWips.get(i).getLocationName());

					branchDetails.setMasterId(branchdetailsWips.get(i).getMasterId());
					branchDetails.setBranchCode(branchdetailsWips.get(i).getBranchCode());
					branchDetails.setBranchName(branchdetailsWips.get(i).getBranchName());

					branchDetails.setCreatedByCode(branchdetailsWips.get(i).getCreatedByCode());
					branchDetails.setCreatedByName(branchdetailsWips.get(i).getCreatedByName());
					branchDetails.setCreatedByEmail(branchdetailsWips.get(i).getCreatedByEmail());
					branchDetails.setCreatedByUsername(branchdetailsWips.get(i).getCreatedByUsername());
					branchDetails.setCreatedByCompanyCode(branchdetailsWips.get(i).getCreatedByCompanyCode());
					branchDetails.setCreatedByCompanyName(branchdetailsWips.get(i).getCreatedByCompanyName());
					branchDetails.setCreatedAt(branchdetailsWips.get(i).getCreatedAt());

					branchDetails.setUpdatedByCode(branchdetailsWips.get(i).getUpdatedByCode());
					branchDetails.setUpdatedByName(branchdetailsWips.get(i).getUpdatedByName());
					branchDetails.setUpdatedByEmail(branchdetailsWips.get(i).getUpdatedByEmail());
					branchDetails.setUpdatedByUsername(branchdetailsWips.get(i).getUpdatedByUsername());
					branchDetails.setUpdatedByCompanyCode(branchdetailsWips.get(i).getUpdatedByCompanyCode());
					branchDetails.setUpdatedByCompanyName(branchdetailsWips.get(i).getUpdatedByCompanyName());
					branchDetails.setUpdatedAt(branchdetailsWips.get(i).getUpdatedAt());

					branchDetails.setSubmittedByCode(branchdetailsWips.get(i).getSubmittedByCode());
					branchDetails.setSubmittedByName(branchdetailsWips.get(i).getSubmittedByName());
					branchDetails.setSubmittedByEmail(branchdetailsWips.get(i).getSubmittedByEmail());
					branchDetails.setSubmittedByUsername(branchdetailsWips.get(i).getSubmittedByUsername());
					branchDetails.setSubmittedByCompanyCode(branchdetailsWips.get(i).getSubmittedByCompanyCode());
					branchDetails.setSubmittedByCompanyName(branchdetailsWips.get(i).getSubmittedByCompanyName());
					branchDetails.setSubmittedAt(branchdetailsWips.get(i).getSubmittedAt());

					branchDetails.setApprovedByCode(currentUser.getEmpCode());
					branchDetails.setApprovedByName(currentUser.getEmpName());
					branchDetails.setApprovedByEmail(currentUser.getEmail());
					branchDetails.setApprovedByUsername(currentUser.getUsername());
					branchDetails.setApprovedByCompanyCode(currentUser.getCompanyCode());
					branchDetails.setApprovedByCompanyName(currentUser.getCompanyName());
					branchDetails.setApprovedAt(new Timestamp(System.currentTimeMillis()));

					// add location
					branchDetailsList.add(i, branchDetails);
				}
				// branch locations list
				branch.setSteps(branchDetailsList);

				branch.setAddress(branchWip.getAddress());
				branch.setContacts(branchWip.getContacts());
				branch.setCompanyCode(branchWip.getCompanyCode());
				branch.setCompanyName(branchWip.getCompanyName());
				branch.setClientCode(branchWip.getClientCode());
				branch.setClientName(branchWip.getClientName());
				branch.setChiefCode(branchWip.getChiefCode());
				branch.setChiefName(branchWip.getChiefName());
				branch.setChiefUserName(branchWip.getChiefUserName());
				branch.setChiefDesig(branchWip.getChiefDesig());
				branch.setChiefMobile(branchWip.getChiefMobile());
				branch.setChiefEmail(branchWip.getChiefEmail());
				branch.setChiefCompanyCode(branchWip.getChiefCompanyCode());
				branch.setChiefCompanyName(branchWip.getChiefCompanyName());
				branch.setRoleCode(branchWip.getRoleCode());
				branch.setRoleName(branchWip.getRoleName());

				branch.setCreatedByCode(branchWip.getCreatedByCode());
				branch.setCreatedByName(branchWip.getCreatedByName());
				branch.setCreatedByEmail(branchWip.getCreatedByEmail());
				branch.setCreatedByUsername(branchWip.getCreatedByUsername());
				branch.setCreatedByCompanyCode(branchWip.getCreatedByCompanyCode());
				branch.setCreatedByCompanyName(branchWip.getCreatedByCompanyName());
				branch.setCreatedAt(branchWip.getCreatedAt());

				branch.setUpdatedByCode(branchWip.getUpdatedByCode());
				branch.setUpdatedByName(branchWip.getUpdatedByName());
				branch.setUpdatedByEmail(branchWip.getUpdatedByEmail());
				branch.setUpdatedByUsername(branchWip.getUpdatedByUsername());
				branch.setUpdatedByCompanyCode(branchWip.getUpdatedByCompanyCode());
				branch.setUpdatedByCompanyName(branchWip.getUpdatedByCompanyName());
				branch.setUpdatedAt(branchWip.getUpdatedAt());

				branch.setSubmittedByCode(branchWip.getSubmittedByCode());
				branch.setSubmittedByName(branchWip.getSubmittedByName());
				branch.setSubmittedByEmail(branchWip.getSubmittedByEmail());
				branch.setSubmittedByUsername(branchWip.getSubmittedByUsername());
				branch.setSubmittedByCompanyCode(branchWip.getSubmittedByCompanyCode());
				branch.setSubmittedByCompanyName(branchWip.getSubmittedByCompanyName());
				branch.setSubmittedAt(branchWip.getSubmittedAt());

				branch.setApprovedByCode(currentUser.getEmpCode());
				branch.setApprovedByName(currentUser.getEmpName());
				branch.setApprovedByEmail(currentUser.getEmail());
				branch.setApprovedByUsername(currentUser.getUsername());
				branch.setApprovedByCompanyCode(currentUser.getCompanyCode());
				branch.setApprovedByCompanyName(currentUser.getCompanyName());
				branch.setApprovedAt(new Timestamp(System.currentTimeMillis()));

				if (branchWip.getActionTypeCode().equals("CREATE")) {
					iBranchDAO.insertDoc(branch);
				} else if (branchWip.getActionTypeCode().equals("EDIT")) {
					iBranchDAO.updateDoc(branch);
				}

				iBranchDAO.deleteWip(uuid);

				/*
				 * for (int i = 0; i < branchLocationWip.size(); i++) {
				 * iBranchDAO.deleteBranchLocationWip(branchLocationWip.get(i).
				 * getId()); }
				 */

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> data = new HashMap<String, String>();
		data.put("outcome", "success");
		return data;
	}

	@Override
	public Map<String, String> reject(Map<String, String[]> request) {
		String outcome = "";
		User currentUser = IUserService.getCurrentUser();
		UUID uuid = UUID.fromString(request.get("id")[0]);
		BranchWip branchWip = iBranchDAO.getWipById(uuid);

		if (branchWip == null) {
			// return "ERROR";
		}

		Workflow wf = workflowService.getWorkflow("BRANCH", branchWip.getActionTypeCode(), "ALL", 0,
				branchWip.getCompanyCode());
		try {
			WorkflowDetails nextStep = workflowService.processWorkflow(wf, branchWip.toReferenceWip(),
					request.get("step_comment")[0], "REJECT", null, currentUser.toApprover());
		} catch (Exception e) {
			e.getMessage();
		}

		branchWip.setWfStatus("DRAFT");
		iBranchDAO.updateWip(branchWip);

		Map<String, String> data = new HashMap<String, String>();
		data.put("outcome", "success");
		return data;
	}

	@Override
	public Map<String, String> requery(Map<String, String[]> request) {
		User currentUser = IUserService.getCurrentUser();
		UUID uuid = UUID.fromString(request.get("id")[0]);
		String stepComment = request.get("step_comment")[0];

		BranchWip branchWip = iBranchDAO.getWipById(uuid);

		if (branchWip == null) {
			// return "ERROR";
		}
		Workflow wf = workflowService.getWorkflow("BRANCH", branchWip.getActionTypeCode(), "ALL", 0,
				branchWip.getCompanyCode());

		try {
			WorkflowDetails wfNextStep = workflowService.processWorkflow(wf, branchWip.toReferenceWip(), stepComment,
					"REQUERY", null, currentUser.toApprover());
		} catch (Exception e) {
			e.getMessage();
		}

		Map<String, String> data = new HashMap<String, String>();
		data.put("outcome", "success");
		return data;
	}

	@Override
	public List<WorkflowRecords> getReviewComment(Map<String, String> parames) {
		List<WorkflowRecords> reviewComments = iBranchDAO.getReviewComment(parames);
		return reviewComments;
	}

	@Override
	public List<BranchWip> findWip(Map<String, String> map) {
		return iBranchDAO.getWips(map);
	}

	@Override
	public ArrayList<String> validateBranch(Map<String, String[]> map) {

		ArrayList<String> errors = new ArrayList<String>();
		if (!Validation.isStrEmpty(map.get("branch_code")[0])) {
			// result.setOutcome("error");
			errors.add("Branch code empty");
		}
		if (!Validation.isStrEmpty(map.get("branch_name")[0])) {
			// result.setOutcome("error");
			errors.add("Branch name empty");
		}
		if (!Validation.isStrEmpty(map.get("chief_code")[0])) {
			// result.setOutcome("error");
			errors.add("Head of branch employee code required");
		}
		if (!Validation.isStrEmpty(map.get("chief_name")[0])) {
			// result.setOutcome("error");
			errors.add("Head of branch employee name required");
		}
		if (!Validation.isStrEmpty(map.get("chief_username")[0])) {
			// result.setOutcome("error");
			errors.add("Head of branch user name required");
		}
		try {
			String[] locId = (String[]) map.get("location_code[]");
			if (!Validation.isStrEmpty(locId[0])) {
				// result.setOutcome("error");
				errors.add("Location name is emapty");
			}
		} catch (Exception e) {
			errors.add("Location emapty");
		}
		UUID id = null;
		try {
			id = UUID.fromString(map.get("id")[0]);
		} catch (Exception e) {
			id = null;
		}
		if (id == null) {
			boolean CheckBranchName = iBranchDAO.validateBranchName(map.get("branch_name")[0]);
			if (!CheckBranchName) {
				errors.add("Branch name must be unique");
			}
			Map<String, String> params = new HashMap<String, String>();
			params.put("branchCode", map.get("branch_code")[0]);
			boolean checkBranchCode = iBranchDAO.validate(params);
			if (!checkBranchCode) {
				errors.add("Branch code must be unique");
			}
		} else {
			boolean CheckBranchName = iBranchDAO.validateWhenUpdateBranchName(id, map.get("branch_name")[0]);
			if (!CheckBranchName) {
				errors.add("Branch name must be unique");
			}
			Map<String, String> params = new HashMap<String, String>();
			params.put("branchCode", map.get("branch_code")[0]);
			boolean checkBranchCode = iBranchDAO.validateWhenUpdateBranchCode(id, map.get("branch_code")[0]);
			if (!checkBranchCode) {
				errors.add("Branch code must be unique");
			}
		}

		return errors;
	}

}
