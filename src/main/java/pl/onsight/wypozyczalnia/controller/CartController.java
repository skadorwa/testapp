package pl.onsight.wypozyczalnia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.onsight.wypozyczalnia.service.ProductOrderService;
import pl.onsight.wypozyczalnia.service.ProductService;
import pl.onsight.wypozyczalnia.service.SessionService;
import pl.onsight.wypozyczalnia.DateFilter;
import pl.onsight.wypozyczalnia.model.Info;
import pl.onsight.wypozyczalnia.model.entity.ProductOrderEntity;
import pl.onsight.wypozyczalnia.model.entity.UserEntity;
import pl.onsight.wypozyczalnia.service.CartService;

import java.util.Date;

@Controller
public class CartController {

    private CartService cartService;
    private ProductOrderService productOrderService;
    private SessionService sessionService;
    private ProductService productService;

    @Autowired
    public CartController(CartService cartService, ProductOrderService productOrderService, SessionService sessionService, ProductService productService) {
        this.cartService = cartService;
        this.productOrderService = productOrderService;
        this.sessionService = sessionService;
        this.productService = productService;
    }

    @GetMapping("/cart")
    public ModelAndView cartTemplate(ModelAndView modelAndView) {
        modelAndView.setViewName("cart");
        modelAndView.addObject("products", productService.countProductsInProductList(cartService.getListOfProductsInCart()));
        modelAndView.addObject("order", new ProductOrderEntity());
        return modelAndView;
    }

    @PostMapping("/makeOrder")
    public ModelAndView makeOrder(@ModelAttribute("order") ProductOrderEntity order,
                                  @RequestParam(value = "datefilter", defaultValue = "") String dateFilter,
                                  ModelAndView modelAndView) {
        modelAndView.setViewName("cart");
        UserEntity user = sessionService.getCurrentUser();
        order.setUser(user);
        order.setProducts(cartService.getListOfProductsInCart());
        order.setOrderStart(new Date(DateFilter.filterData(dateFilter)[0]));
        order.setOrderEnd(new Date(DateFilter.filterData(dateFilter)[1]));

        if (productService.isOrderAvailableToSave(order)) {
            modelAndView.addObject("info", new Info("Zamówienie dodane poprawnie!", true));
            productOrderService.saveOrder(order);
            cartService.removeProductFromCart();
        } else {
            modelAndView.addObject("info", new Info("Zamówienie niepoprawne", false));
        }

        return cartTemplate(modelAndView);
    }
}