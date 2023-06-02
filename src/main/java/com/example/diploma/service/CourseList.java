package com.example.diploma.service;

import com.example.diploma.entity.Course;
import com.example.diploma.repo.CourseRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@Route("courses")
public class CourseList extends AppLayout {
    VerticalLayout layout;
    Grid<Course> grid;
    RouterLink linkCreate;

    @Autowired
    CourseRepository courseRepository;

    public CourseList() {
        layout = new VerticalLayout();
        grid = new Grid<>();
        linkCreate = new RouterLink("Створити курс", CreateCourse.class, 0L);
        layout.add(linkCreate);
        layout.add(grid);
        addToNavbar(new H3("Список курсів"));
        setContent(layout);
    }

    @PostConstruct
    public void fillGrid() {
        List<Course> courses = courseRepository.findAll();
        if (!courses.isEmpty()) {

            grid.addColumn(Course::getTitle).setHeader("Назва");
            grid.addColumn(Course::getDescription).setHeader("Опис");
            grid.addColumn(new NativeButtonRenderer<>("Редагувати", contact -> UI.getCurrent().navigate(CreateCourse.class, contact.getId())));

            grid.setItems(courses);
        }
    }

}
