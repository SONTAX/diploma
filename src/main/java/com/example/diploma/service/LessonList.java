package com.example.diploma.service;

import com.example.diploma.entity.Lesson;
import com.example.diploma.repo.LessonRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@Route("lessons")
public class LessonList  extends AppLayout {
    VerticalLayout layout;
    Grid<Lesson> grid;
    RouterLink linkCreate;

    @Autowired
    LessonRepository lessonRepository;

    public LessonList() {
        layout = new VerticalLayout();
        grid = new Grid<>();
        linkCreate = new RouterLink("Створити урок", CreateLesson.class, 0L);
        layout.add(linkCreate);
        layout.add(grid);
        addToNavbar(new H3("Список уроків"));
        setContent(layout);
    }

    @PostConstruct
    public void fillGrid() {
        List<Lesson> lessons = lessonRepository.findAll();
        if (!lessons.isEmpty()) {
            grid.addColumn(Lesson::getTitle).setHeader("Назва");
            grid.addColumn(Lesson::getTerm).setHeader("Кінцева дата виконання");
            grid.addColumn(new NativeButtonRenderer<>("Редагувати", lesson -> UI.getCurrent().navigate(CreateLesson.class, lesson.getId())));
            grid.addColumn(new NativeButtonRenderer<>("Видалити", lesson -> {
                Dialog dialog = new Dialog();
                Button confirm = new Button("Видалити");
                Button cancel = new Button("Відмінити");
                dialog.add("Впевнені, що хочете видалити урок?");
                dialog.add(confirm);
                dialog.add(cancel);

                confirm.addClickListener(clickEvent -> {
                    lessonRepository.delete(lesson);
                    dialog.close();
                    Notification notification = new Notification("Урок видалено", 1000);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();
                    grid.setItems(lessonRepository.findAll());

                });

                cancel.addClickListener(clickEvent -> dialog.close());

                dialog.open();
            }));
            grid.setItems(lessons);

            // grid.addItemClickListener(lesson -> Notification.show(String.format("File location: %s", lesson.getItem().getTitle())));

        }
    }

}
