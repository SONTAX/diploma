package com.example.diploma.service;

import com.example.diploma.entity.Course;
import com.example.diploma.entity.CourseUser;
import com.example.diploma.entity.User;
import com.example.diploma.repo.CourseRepository;
import com.example.diploma.repo.CourseUserRepository;
import com.example.diploma.repo.UserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route("manageCourse")
public class CreateCourse extends AppLayout implements HasUrlParameter<Long> {

    VerticalLayout layout;
    Long id;
    FormLayout courseForm;
    TextField title;
    TextArea description;

    Grid<User> grid;

    Button saveCourse;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseUserRepository courseUserRepository;

    public CreateCourse() {
        layout = new VerticalLayout();
        courseForm = new FormLayout();
        title = new TextField("Назва курсу");
        description = new TextArea("Опис курсу");
        saveCourse = new Button("Зберегти");
        grid = new Grid<>();
        courseForm.add(title, description, saveCourse);
        courseForm.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        layout.add(courseForm);
        layout.add(grid);
        setContent(layout);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long courseId) {
        id = courseId;
        if (!id.equals(0)) {
            if (!id.equals(0)) {
                layout.add(grid);
            }
            addToNavbar(new H3("Створення курсу"));
        } else {
            addToNavbar(new H3("Редагування курсу"));
        }
        fillForm();
    }

    public void fillForm() {

        if (!id.equals(0)) {
            Optional<Course> course = courseRepository.findById(id);
            course.ifPresent(x -> {
                title.setValue(x.getTitle());
                description.setValue(x.getDescription());
            });
        }

        saveCourse.addClickListener(clickEvent -> {
            Course course = new Course();
            if (!id.equals(0)) {
                course.setId(id);
            }
            course.setTitle(title.getValue());
            course.setDescription(description.getValue());
            courseRepository.save(course);

            Notification notification = new Notification(!id.equals(0) ? "Курс було успішно створено" : "Курс було змінено", 1000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.addDetachListener(detachEvent -> UI.getCurrent().navigate(CourseList.class));
            courseForm.setEnabled(false);
            notification.open();
        });
    }

    @PostConstruct
    public void fillGrid() {
        List<CourseUser> courseUsers = courseUserRepository.findAllByCourseId(id);
        List<User> users = new ArrayList<>();
        for (CourseUser courseUser : courseUsers) {
            userRepository.findById(courseUser.getUserId()).ifPresent(users::add);
        }
        if (!users.isEmpty()) {
            grid.addColumn(User::getUsername).setHeader("Ім'я");
            grid.addColumn(new NativeButtonRenderer<>("Відрахувати з курсу", student -> {
                Dialog dialog = new Dialog();
                Button confirm = new Button("Видалити");
                Button cancel = new Button("Відмінити");
                dialog.add("Ви впевненні, що хочете відрахувати з курсу?");
                dialog.add(confirm);
                dialog.add(cancel);
                confirm.addClickListener(clickEvent -> {
                    courseUserRepository.deleteByUserIdAndCourseId(student.getId(), id);
                    dialog.close();
                    Notification notification = new Notification("Студента відраховано", 1000);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();

                    users.remove(student);
                    grid.setItems(users);

                });

                cancel.addClickListener(clickEvent -> dialog.close());

                dialog.open();

            }));

            grid.setItems(users);
        }
    }
}
