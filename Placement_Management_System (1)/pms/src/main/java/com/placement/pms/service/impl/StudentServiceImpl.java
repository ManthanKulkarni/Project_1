package com.placement.pms.service.impl;

import com.placement.pms.dto.StudentDTO;
import com.placement.pms.entity.Skill;
import com.placement.pms.entity.Student;
import com.placement.pms.exception.DuplicateResourceException;
import com.placement.pms.exception.ResourceNotFoundException;
import com.placement.pms.repository.SkillRepository;
import com.placement.pms.repository.StudentRepository;
import com.placement.pms.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final SkillRepository skillRepository;

    @Override
    public StudentDTO createStudent(StudentDTO dto) {
        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("A student already exists with email: " + dto.getEmail());
        }
        Student student = Student.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .department(dto.getDepartment())
                .cgpa(dto.getCgpa())
                .resumeSummary(dto.getResumeSummary())
                .active(true)
                .skills(resolveSkills(dto.getSkills()))
                .build();
        return toDTO(studentRepository.save(student));
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long id) {
        return toDTO(findStudentOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentDTO> getAllStudents(String name, Pageable pageable) {
        Page<Student> page = (name == null || name.isBlank())
                ? studentRepository.findAll(pageable)
                : studentRepository.findByNameContainingIgnoreCase(name, pageable);
        return page.map(this::toDTO);
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO dto) {
        Student student = findStudentOrThrow(id);

        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(student.getEmail())
                && studentRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("A student already exists with email: " + dto.getEmail());
        }

        if (dto.getName() != null) student.setName(dto.getName());
        if (dto.getEmail() != null) student.setEmail(dto.getEmail());
        if (dto.getDepartment() != null) student.setDepartment(dto.getDepartment());
        if (dto.getCgpa() != null) student.setCgpa(dto.getCgpa());
        if (dto.getResumeSummary() != null) student.setResumeSummary(dto.getResumeSummary());
        if (dto.getSkills() != null) student.setSkills(resolveSkills(dto.getSkills()));

        return toDTO(studentRepository.save(student));
    }

    @Override
    public void deactivateStudent(Long id) {
        Student student = findStudentOrThrow(id);
        student.setActive(false);
        studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = findStudentOrThrow(id);
        studentRepository.delete(student);
    }

    private Student findStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }

    private Set<Skill> resolveSkills(Set<String> skillNames) {
        if (skillNames == null) return new HashSet<>();
        return skillNames.stream()
                .filter(s -> s != null && !s.isBlank())
                .map(name -> skillRepository.findByNameIgnoreCase(name.trim())
                        .orElseGet(() -> skillRepository.save(Skill.builder().name(name.trim()).build())))
                .collect(Collectors.toSet());
    }

    private StudentDTO toDTO(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .name(student.getName())
                .email(student.getEmail())
                .department(student.getDepartment())
                .cgpa(student.getCgpa())
                .resumeSummary(student.getResumeSummary())
                .active(student.isActive())
                .skills(student.getSkills().stream().map(Skill::getName).collect(Collectors.toSet()))
                .build();
    }
}
