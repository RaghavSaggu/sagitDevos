package com.sagitDevos.sagitDevos.controller;

import com.sagitDevos.sagitDevos.model.EmployeeDataObject;
import com.sagitDevos.sagitDevos.model.SagitException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @Value("${server.port}")
    private String serverPort;

    @RequestMapping("home")
    public String home(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String name = req.getParameter("aname");
        System.out.println("Home clicked by " + name + " on server " + serverPort);
        session.setAttribute("name", StringUtils.isEmpty(name) ? "Unknown" : name);
        return "homepage.jsp";
    }

    @RequestMapping("home1")
    public String home1(@RequestParam(required = false) String name, Model model) {
        System.out.println("Home clicked by " + name);
        model.addAttribute("name", StringUtils.isEmpty(name) ? "Unknown" : name);
        return "home";
    }

    @RequestMapping("home2")
    public ModelAndView home2(@RequestParam(required = false) String name) {
        System.out.println("Home clicked by " + name);
        ModelAndView mv = new ModelAndView();
        mv.addObject("name", StringUtils.isEmpty(name) ? "Unknown" : name);
        mv.setViewName("home");
        return mv;
    }

    @RequestMapping("home3")
    public ModelAndView home3(EmployeeDataObject dataObject) {
        ModelAndView mv = new ModelAndView();
        try {
            System.out.println("Home clicked by " + dataObject.getName());
            if (dataObject == null || (StringUtils.isEmpty(dataObject.getName()) && StringUtils.isEmpty(dataObject.getDepartment()))) {
                throw new SagitException("Object not found");
            }
            mv.addObject("obj", dataObject);
            mv.setViewName("home");
        } catch (SagitException e) {
            mv.addObject("msg", e.getMessage());
            mv.setViewName("Error");
        } catch (Exception e) {
            mv.addObject("msg", "Something went wrong");
            mv.setViewName("Error");
        }
        return mv;
    }

}
