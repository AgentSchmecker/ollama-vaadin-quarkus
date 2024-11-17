package de.teampb.soco.llm.guitester.service.data;

import de.teampb.soco.llm.guitester.entities.Order;
import de.teampb.soco.llm.guitester.entities.OrderStatus;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<Order> {

    @Tool("Sucht alle Bestellungen, welche im übergebenen Status sind")
    public List<Order> findByStatusOpen(@P("Status, nach dem die offenen Bestellungen gesucht werden sollen") OrderStatus orderStatus){
        return list("status",orderStatus);
    }

}
