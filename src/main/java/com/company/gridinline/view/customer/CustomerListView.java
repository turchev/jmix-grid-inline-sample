package com.company.gridinline.view.customer;

import com.company.gridinline.entity.Customer;
import com.company.gridinline.view.main.MainView;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.grid.editor.EditorSaveEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.checkbox.JmixCheckbox;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "customers", layout = MainView.class)
@ViewController("Customer.list")
@ViewDescriptor("customer-list-view.xml")
@LookupComponent("customersDataGrid")
@DialogMode(width = "64em")
public class CustomerListView extends StandardListView<Customer> {
    @ViewComponent
    protected DataGrid<Customer> customersDataGrid;
    @Autowired
    protected Notifications notifications;
    @Autowired
    protected DataManager dataManager;
    @ViewComponent
    private CollectionContainer<Customer> customersDc;


    @Subscribe(id = "createBtn", subject = "clickListener")
    public void onCreateBtnClick(final ClickEvent<JmixButton> event) {
        customersDc.getMutableItems().add(dataManager.create(Customer.class));
    }

// Рабочий способ
//    @Subscribe
//    public void onInit(final InitEvent event) {
//        DataGridEditor<Customer> editor = customersDataGrid.getEditor();
//        editor.addSaveListener(saveEvent -> dataManager.save(saveEvent.getItem()));
//    }

    //  Оба способа рабочих, но на мой взгляд этот меньше засоряет код, так как выведен в отдельный метод
    @Install(to = "customersDataGrid.@editor", subject = "saveListener")
    protected void onCustomersDataGridEditorSaved(EditorSaveEvent<Customer> event) {
        dataManager.save(event.getItem());
    }


    @Subscribe("editorBufferedCheckbox")
    protected void onEditorBufferedCheckboxValueChange(ComponentValueChangeEvent<JmixCheckbox, Boolean> event) {
        if (!event.isFromClient()) {
            return;
        }

        if (customersDataGrid.getEditor().isOpen()) {
            notifications.create("Please close Editor before changing its mode")
                    .withType(Notifications.Type.WARNING)
                    .withCloseable(false)
                    .show();

            event.getSource().setValue(event.getOldValue());
        } else {
            customersDataGrid.getEditor().setBuffered(event.getValue());

            customersDataGrid.getColumnByKey("bufferedEditorColumn").setVisible(event.getValue());
            customersDataGrid.getColumnByKey("nonBufferedEditorColumn").setVisible(!event.getValue());
        }
    }

}
