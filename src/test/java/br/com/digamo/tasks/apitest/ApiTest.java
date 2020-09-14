package br.com.digamo.tasks.apitest;

import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class ApiTest {

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-api";
	}
	
	@Test
	public void shouldReturnTasks() {
		RestAssured.given()
			.when()
				.get("/todo")
			.then()
				.statusCode(200)
			;
	}
	
	@Test
	public void shouldSaveTaskSuccessfully() {
		RestAssured.given()
				.body("{ \"name\": \"Task Test\", \"dueDate\": \"2030-01-01\" }")
				.contentType(ContentType.JSON)
			.when()
				.post("/todo")
			.then()
				.statusCode(201)
			;
	}

	@Test
	public void shouldNotSaveTaskWithEmptyName() {
		RestAssured.given()
				.body("{ \"name\": \"\", \"dueDate\": \"2030-01-01\" }")
				.contentType(ContentType.JSON)
			.when()
				.post("/todo")
			.then()
				.statusCode(400)
				.body("message", CoreMatchers.equalTo(Arrays.asList("Fill the task description.")))
				
			;
	}

	@Test
	public void shouldNotSaveTaskWithEmptyDate() {
		RestAssured.given()
				.body("{ \"name\": \"Task Test\", \"dueDate\": \"\" }")
				.contentType(ContentType.JSON)
			.when()
				.post("/todo")
			.then()
				.statusCode(400)
				.body("message", CoreMatchers.equalTo(Arrays.asList("Fill the due date.")))
				
			;
	}

	@Test
	public void shouldNotSaveTaskWithDateLast() {
		RestAssured.given()
				.body("{ \"name\": \"Task Test\", \"dueDate\": \"2010-01-01\" }")
				.contentType(ContentType.JSON)
			.when()
				.post("/todo")
			.then()
				.statusCode(400)
				.body("message", CoreMatchers.equalTo(Arrays.asList("Due date must not be in past.")))
				
			;
	}

	@Test
	public void shouldDeleteTaskSuccessfully() {
		
		//save
		Integer id = RestAssured.given()
				.body("{ \"name\": \"Task Test to delete\", \"dueDate\": \"2030-01-01\" }")
				.contentType(ContentType.JSON)
			.when()
				.post("/todo")
			.then()
				.statusCode(201)
				.extract().path("id")
			;
		
		//delete
		RestAssured.given()
			.when()
				.delete("/todo/"+ id)
			.then()
				.statusCode(204)
			;
		
	}

}


 

