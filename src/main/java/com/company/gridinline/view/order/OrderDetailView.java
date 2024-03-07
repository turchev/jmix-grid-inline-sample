package com.company.gridinline.view.order;

import com.company.gridinline.entity.Order;
import com.company.gridinline.entity.OrderLine;
import com.company.gridinline.view.main.MainView;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.data.Sequence;
import io.jmix.data.Sequences;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Objects;

@Route(value = "orders/:id", layout = MainView.class)
@ViewController("Order_.detail")
@ViewDescriptor("order-detail-view.xml")
@EditedEntityContainer("orderDc")
public class OrderDetailView extends StandardDetailView<Order> {
    @ViewComponent
    private DataGrid<OrderLine> orderLinesDataGrid;
    @ViewComponent
    private CollectionPropertyContainer<OrderLine> orderLinesDc;
    @Autowired
    private Sequences sequences;
    @ViewComponent
    private DataContext dataContext;

    @Subscribe
    public void onInitEntity(final InitEntityEvent<Order> event) {
        Long number = sequences.createNextValue(Sequence.withName("order_number"));
        event.getEntity().setNumber(number);
    }

    @Subscribe("orderLinesDataGrid.create")
    public void onOrderLinesDataGridCreate(final ActionPerformedEvent event) {
        OrderLine orderLine = dataContext.create(OrderLine.class);
        orderLine.setOrder(getEditedEntity());
        orderLinesDc.getMutableItems().add(orderLine);
        orderLinesDataGrid.select(orderLine);
        orderLinesDataGrid.getEditor().editItem(orderLine);
    }

    @Subscribe("orderLinesDataGrid.edit")
    public void onOrderLinesDataGridEdit(final ActionPerformedEvent event) {
        openItemForEditing();
    }

    @Subscribe("orderLinesDataGrid")
    public void onOrderLinesDataGridItemClick(final ItemClickEvent<OrderLine> event) {
        openItemForEditing();
    }

    private void openItemForEditing() {
        OrderLine selectedOrderLine = orderLinesDataGrid.getSingleSelectedItem();
        if (selectedOrderLine != null && !orderLinesDataGrid.getEditor().isOpen()) {
            orderLinesDataGrid.getEditor().editItem(selectedOrderLine);
        }
    }

    @Subscribe
    public void onBeforeSave(final BeforeSaveEvent event) {
        BigDecimal costs = orderLinesDc.getItems().stream()
                .map(OrderLine::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        getEditedEntity().setCosts(costs);
    }
}