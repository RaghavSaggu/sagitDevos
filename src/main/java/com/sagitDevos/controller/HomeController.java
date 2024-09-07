package com.sagitDevos.controller;

import com.sagitDevos.model.dataObjects.EmployeeDataObject;
import com.sagitDevos.model.exceptions.UserDefinedSagitException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class HomeController {
    @Value("${server.port}")
    private String serverPort;

    @RequestMapping("home")
    public String home(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String name = req.getParameter("aname");
        log.debug("Home clicked by " + name + " on server " + serverPort);
        session.setAttribute("name", StringUtils.isEmpty(name) ? "Unknown" : name);
        return "homepage.jsp";
    }

    @RequestMapping("home1")
    public String home1(@RequestParam(required = false) String name, Model model) {
        log.debug("Home1 clicked by " + name);
        model.addAttribute("name", StringUtils.isEmpty(name) ? "Unknown" : name);
        return "home";
    }

    @RequestMapping("home2")
    public ModelAndView home2(@RequestParam(required = false) String name) {
        log.debug("Home2 clicked by " + name);
        ModelAndView mv = new ModelAndView();
        mv.addObject("name", StringUtils.isEmpty(name) ? "Unknown" : name);
        mv.setViewName("home");
        return mv;
    }

    @RequestMapping("home3")
    public ModelAndView home3(EmployeeDataObject dataObject) {
        ModelAndView mv = new ModelAndView();
        try {
            log.debug("Home3 clicked");
            if (dataObject == null || (StringUtils.isEmpty(dataObject.getName()) && StringUtils.isEmpty(dataObject.getDepartment()))) {
                throw new UserDefinedSagitException("Object not found");
            }
            mv.addObject("obj", dataObject);
            mv.setViewName("home");
        } catch (UserDefinedSagitException e) {
            mv.addObject("msg", e.getMessage());
            mv.setViewName("Error");
        } catch (Exception e) {
            mv.addObject("msg", "Something went wrong");
            mv.setViewName("Error");
        }
        return mv;
    }

}
