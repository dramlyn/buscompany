package buscompany.controller;

import buscompany.dto.request.OrderDtoRequest;
import buscompany.dto.response.OrderDtoResponse;
import buscompany.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderDtoResponse insertOrder(@RequestBody @Valid OrderDtoRequest orderDtoRequest, @CookieValue("JAVASESSIONID") String sessionID){
        return orderService.insertOrder(orderDtoRequest, sessionID);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrderDtoResponse> getOrderList(@RequestParam(value = "fromStation", required = false) String fromStation,
                                               @RequestParam(value = "toStation", required = false) String toStation,
                                               @RequestParam(value = "busName", required = false) String busName,
                                               @RequestParam(value = "fromDate", required = false) String fromDate,
                                               @RequestParam(value = "toDate", required = false) String toDate,
                                               @RequestParam(value = "clientId", required = false, defaultValue = "0") int clientID,
                                               @CookieValue("JAVASESSIONID") String sessionID){
        return orderService.getOrderList(fromStation, toStation, busName, fromDate, toDate, clientID, sessionID);

    }

    @DeleteMapping(value ="/{orderID}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public void cancelOrder(@CookieValue("JAVASESSIONID") String sessionID, @PathVariable("orderID") int orderID){
        orderService.cancelOrder(sessionID, orderID);
    }

}
