package com.example.crud_test_demo;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.crud_test_demo.controllers.StudentController;
import com.example.crud_test_demo.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class StudentControllerTests {

	@Autowired
	private StudentController studentController;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void StudentControllerLoads() {
		assertThat(studentController).isNotNull();
	}

	private Student getStudentFromId(Integer id) throws Exception {
		MvcResult result = this.mockMvc.perform(get("/student/" + id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		try {
			String studentJSON = result.getResponse().getContentAsString();
			Student student = objectMapper.readValue(studentJSON, Student.class);

			assertThat(student).isNotNull();
			assertThat(student.getId()).isNotNull();

			return student;

		} catch (Exception e) {
			return null;
		}
	}

	private Student createStudent() throws Exception {
		Student student = new Student();
		student.setWorking(true);
		student.setName("Erika");
		student.setSurname("Longo");

		return createStudent(student);
	}

	private Student createStudent(Student student) throws Exception {
		MvcResult result = createStudentRequest();
		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);

		assertThat(studentFromResponse).isNotNull();
		assertThat(studentFromResponse.getId()).isNotNull();

		return studentFromResponse;
	}

	private MvcResult createStudentRequest() throws Exception {
		Student student = new Student();
		student.setWorking(true);
		student.setName("Erika");
		student.setSurname("Longo");

		return createStudentRequest(student);
	}

	private MvcResult createStudentRequest(Student student) throws Exception {
		if(student == null) return null;

		String studentJSON = objectMapper.writeValueAsString(student);

		return this.mockMvc.perform(post("/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	void createStudentTest() throws Exception {
		Student studentFromResponse = createStudent();
	}

	@Test
	void readStudentsList() throws Exception {
		createStudentRequest();

		MvcResult result = this.mockMvc.perform(get("/student/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		List<Student> studentsFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
		System.out.println("Students in database: " + studentsFromResponse.size());
		assertThat(studentsFromResponse.size()).isNotZero();
	}

	@Test
	void readSingleStudent() throws Exception {
		Student student = createStudent();
		Student studentFromResponse = getStudentFromId(student.getId());
		assertThat(studentFromResponse).isNotNull();
		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
	}

	@Test
	void updateUser() throws Exception {
		Student student = createStudent();

		String newName = "Andrea";
		student.setName(newName);
		String studentJSON = objectMapper.writeValueAsString(student);

		MvcResult result = this.mockMvc.perform(put("/student/" + student.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);

		//Check student from PUT
		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponse.getName()).isEqualTo(newName);

		//I get the student with GET
		Student studentFromResponseGet = getStudentFromId(student.getId());
		assertThat(studentFromResponseGet.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponse.getName()).isEqualTo(newName);
	}

	@Test
	void deleteStudent() throws Exception {
		Student student = createStudent();
		assertThat(student.getId()).isNotNull();

		this.mockMvc.perform(delete("/student/" + student.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponseGet = getStudentFromId(student.getId());
		assertThat(studentFromResponseGet).isNull();
	}

	@Test
	void activateStudent() throws Exception {
		Student student = createStudent();
		assertThat(student.getId()).isNotNull();

		MvcResult result = this.mockMvc.perform(put("/student/" + student.getId() + "/activation?activated=true"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertThat(studentFromResponse).isNotNull();
		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponse.getWorking()).isEqualTo(true);

		Student studentFromResponseGet = getStudentFromId(student.getId());
		assertThat(studentFromResponseGet).isNotNull();
		assertThat(studentFromResponseGet.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponseGet.getWorking()).isEqualTo(true);
	}
}