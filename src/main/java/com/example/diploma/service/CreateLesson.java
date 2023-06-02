package com.example.diploma.service;

import com.example.diploma.entity.Lesson;
import com.example.diploma.entity.LessonType;
import com.example.diploma.repo.CourseUserRepository;
import com.example.diploma.repo.LessonRepository;
import com.example.diploma.repo.UserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Route("manageLesson")
public class CreateLesson extends AppLayout implements HasUrlParameter<Long> {
    Long id;
    FormLayout lessonForm;
    TextField title;
    TextArea content;

    ComboBox<LessonType> lessonType;

    DateTimePicker date;

    TextArea task;

    Button saveLesson;

    @Autowired
    LessonRepository lessonRepository;

    public CreateLesson() {
        lessonForm = new FormLayout();
        title = new TextField("Назва уроку");
        content = new TextArea("Навчальний матеріал");
        lessonType = new ComboBox<>("Тип уроку");
        lessonType.setItems(LessonType.DEFAULT, LessonType.TEST);
        date = new DateTimePicker("Термін виконання");
        date.setValue(LocalDateTime.now());
        task = new TextArea("Самойстійне завдання");
        saveLesson = new Button("Зберегти");
        lessonForm.add(title, content, lessonType, date, task, saveLesson);
        lessonForm.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        setContent(lessonForm);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long lessonId) {
        id = lessonId;
        if (!id.equals(0)) {
            addToNavbar(new H3("Створення уроку"));
        } else {
            addToNavbar(new H3("Редагування уроку"));
        }
        fillForm();
    }

    public void fillForm() {

        if (!id.equals(0)) {
            Optional<Lesson> lesson = lessonRepository.findById(id);
            lesson.ifPresent(x -> {
                title.setValue(x.getTitle());
                content.setValue(x.getContent());
                lessonType.setValue(x.getLessonType());
                date.setValue(x.getTerm());
                task.setValue(x.getTask());
            });
        }

        saveLesson.addClickListener(clickEvent -> {
            Lesson lesson = new Lesson();
            if (!id.equals(0)) {
                lesson.setId(id);
            }
            lesson.setCourseId(0L);
            lesson.setTitle(title.getValue());
            lesson.setContent(content.getValue());
            lesson.setLessonType(lessonType.getValue());
            lesson.setTerm(date.getValue());
            lesson.setTask(task.getValue());
            lessonRepository.save(lesson);

            Notification notification = new Notification(!id.equals(0) ? "Урок було успішно створено" : "Урок було змінено", 1000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.addDetachListener(detachEvent -> UI.getCurrent().navigate(LessonList.class));
            lessonForm.setEnabled(false);
            notification.open();
        });
    }
}
