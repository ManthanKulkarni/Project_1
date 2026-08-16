package com.placement.pms.service;

import com.placement.pms.dto.StudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {
    StudentDTO createStudent(StudentDTO dto);
    StudentDTO getStudentById(Long id);
    Page<StudentDTO> getAllStudents(String name, Pageable pageable);
    StudentDTO updateStudent(Long id, StudentDTO dto);
    void deactivateStudent(Long id);
    void deleteStudent(Long id);
}
