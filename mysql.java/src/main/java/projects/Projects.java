package projects;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.dao.DdbConnection;
import projects.entities.Project;
import projects.exceptions.DdbExceptions;
import projects.service.ProjectService;


public class Projects {
	
	ProjectService projectService= new ProjectService();
	private Project curProject;
 // @formatter:off
	private List<String> operations = List.of(
			"1) Add a Project ",
			"2) List projects",
			"3) Select a project",
	        "4) Update project details",
	        "5) Delete a project");
// @formatter:on	
	private Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		
	 new Projects().processUserSelections();
		
 
	}    
	
	private void processUserSelections() {
		boolean done= false;
		
		while(!done) {
			try {
				int selection = getUserSelection();
				switch(selection) { 
				case -1:
				done= exitMenu();
					break;
				case 1:
					createProject();
					break;
				case 2:
					listProjects();
					break;
					
				case 3:
					selectProject();
					break;
				case 4:
					updateProjectDetail();
					break;
				case 5:
					deleteProject();
					break;
					
					default:
						System.out.println("\n"+ selection +"is not a valid selcection. PLease try again. ");
						break;
				}
				}
					
			
			
			catch (Exception e){
				System.out.println("\nError : "+ e + "try again " );
			}
		}
		
	}

	

	private void deleteProject() {
		listProjects();
		Integer projectId= getIntInput("Enter the Id of The Project to Delete ");
		projectService.deleteProject(projectId);
		System.out.println("project "+ projectId + " was deleted succesfully " );
		
		if(Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)){
				curProject= null;
		}
	
		
	}

	private void updateProjectDetail() {
		
		if (Objects.isNull(curProject)){
		System.out.println("\nPlease select a Project ");
		return;
		}
		String projectName= getStringInput("Enter the project name [" + curProject.getProjectName()+ "]");
		
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours()+ "]");
		

		BigDecimal actualHours = getDecimalInput("Enter the actual hours [" + curProject.getActualHours()+ "]");
		
	Integer difficulty= getIntInput("Enter the project difficulty(1-5)[" + curProject.getDifficulty()+ "]");
	
	String notes = getStringInput("Enter the project notes [" + curProject.getNotes()+ "]");
	Project project= new Project();
	
	project.setProjectId(curProject.getProjectId());
	project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName():projectName);
	project.setEstimatedHours(Objects.isNull(estimatedHours) ?curProject.getEstimatedHours():estimatedHours);
	project.setActualHours(Objects.isNull(actualHours) ?curProject.getActualHours():actualHours);
	project.setDifficulty(Objects.isNull(difficulty) ?curProject.getDifficulty():difficulty);
	project.setNotes(Objects.isNull(notes)?curProject.getNotes(): notes);
	
	
	
	 projectService.modifyProjectDetails(project);
	 curProject = projectService.fetchProjectById(curProject.getProjectId());
	 
		
	}

	private void selectProject() {
		listProjects();
		Integer projectId = getIntInput("Enter a project Id to select a project");
		curProject= null;
		curProject= projectService.fetchProjectById(projectId);
		System.out.println(curProject);
	}

	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		System.out.println("\n  Projects");
		projects.forEach(project-> System.out.println("  " + project.getProjectId() + ": " + project.getProjectName()));
	}

	private void createProject() {
		String projectName= getStringInput("Enter the project name");
		BigDecimal estimatedHours= getDecimalInput("Enter the estimated Hours");
		BigDecimal actualHours= getDecimalInput("Enter the actual Hours");
		Integer difficulty = getIntInput("Enter the project difficulty(1-5)");
		String notes= getStringInput("Enter the project notes");
		
		Project project= new Project();
		
		 project.setProjectName(projectName);
		 project.setEstimatedHours(estimatedHours);
		 project.setActualHours(actualHours);
		 project.setDifficulty(difficulty);
		 project.setNotes(notes);
		
		 Project dbProject= projectService.addProject(project);
		 
		 System.out.println("You have succesfullty created poject "+ dbProject);
	}

	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		if(Objects.isNull(input)) {
			return null;
		}
		try {
			return new BigDecimal(input).setScale(2);
		}
		catch(NumberFormatException e){
			throw new DdbExceptions(input + " is not a valid decimal number"); 
		}
	}

	private boolean exitMenu() {
		
		return true;
	}

	private int getUserSelection() {
		printOperatins();
		
		Integer input= getIntInput("Enter a Menu Selection");
		return Objects.isNull(input)? -1:input;
	}

	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		if(Objects.isNull(input)) {
			return null;
		}
		try {
			return Integer.valueOf(input);
		}
		catch(NumberFormatException e){
			throw new DdbExceptions(input + " is not a valid number"); 
		}
		
	}

	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();
		return input.isBlank()? null: input.trim();
	}

	private void printOperatins() {
		System.out.println("\n   This are the available selections. press the Enter key to exit:");
		for(String op:operations) {
			System.out.println("\n" + op);
		}
			if(Objects.isNull(curProject)) {
				System.out.println("\n you are not working with a project");
				
			}
			else {
				System.out.println("you are working with a project "+ curProject);
			}
		}
		
	

}
