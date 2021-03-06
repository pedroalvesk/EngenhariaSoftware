package edu.ufp.nk.ws1.controllers;

import edu.ufp.nk.ws1.models.Course;
import edu.ufp.nk.ws1.models.Degree;
import edu.ufp.nk.ws1.services.CourseService;
import edu.ufp.nk.ws1.services.DegreeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Controller
@RequestMapping("/course")
public class CourseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private CourseService courseService;
    private DegreeService degreeService;

    // Constructor
    @Autowired
    public CourseController(CourseService courseService, DegreeService degreeService) {
        this.courseService = courseService;
        this.degreeService = degreeService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Course>> getAllCourses() {
        this.logger.info("Received a get request");

        return ResponseEntity.ok(this.courseService.findAll());
    }

    @RequestMapping(value = "/id={id}", method = RequestMethod.GET)
    public ResponseEntity<Course> getCourseById(@PathVariable("id") long id) throws NoCourseException {
        this.logger.info("Received a get request");

        Optional<Course> optionalCourse = this.courseService.findById(id);
        if (optionalCourse.isPresent()) {
            return ResponseEntity.ok(optionalCourse.get());
        }
        throw new NoCourseException(id);
    }

    @PostMapping(value = "/{degree}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> createCourseByDegree(@RequestBody Course course, @PathVariable Long degree) {
        this.logger.info("Received a post request");

        Optional<Course> courseOptional = this.courseService.createCourseByDegree(course, degree);
        if (courseOptional.isPresent()) {
            return ResponseEntity.ok(courseOptional.get());
        }
        Optional<Degree> degreeOptional = degreeService.findById(degree);
        if (degreeOptional.isPresent()) {
            throw new CourseAlreadyExistsException(course.getName());
        } else throw new NoDegreeException(degree);
    }

    /*
     * EXCEPTIONS
     */

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such course")
    private static class NoCourseException extends RuntimeException {
        public NoCourseException(Long id) {
            super("No such course with id: " + id);
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Course already exists")
    private static class CourseAlreadyExistsException extends RuntimeException {
        public CourseAlreadyExistsException(String name) {
            super("A course with name: " + name + " already exists");
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such degree")
    private static class NoDegreeException extends RuntimeException {
        public NoDegreeException(Long id) {
            super("No such degree with id: " + id);
        }
    }
}
