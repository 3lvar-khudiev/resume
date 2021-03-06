/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.controller;

import com.company.dao.inter.CountryDaoInter;
import com.company.dao.inter.UserDaoInter;
import com.company.entity.Authority;
import com.company.entity.Country;
import com.company.entity.User;
import com.company.main.Context;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author HP
 */
@WebServlet(name = "UserCreateController", urlPatterns = {"/user-create"})
public class UserCreateController extends HttpServlet {

    private UserDaoInter userDao = Context.instanceUserDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action.equals("create")) {
            User user = new User();

            String name = request.getParameter("name");                     // label-lerden melumatlar alinir
            String surname = request.getParameter("surname");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String profileDescription = request.getParameter("profile-description");
            String address = request.getParameter("address");
//            String authority = request.getParameter("authority");
            String datepicker = request.getParameter("datepicker");
            String birthPlace = request.getParameter("birthPlace");
            String nationality = request.getParameter("nationality");

            if (datepicker != null && !datepicker.equals("")) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(datepicker);     // String sql.Date-e chevrilir
                user.setBirthdate(sqlDate);
            }

            CountryDaoInter countryDao = Context.instanceCountryDao();
            List<Country> countries = countryDao.getAll();                    // country ve nationality-ler bazadan chekilir

            for (Country c : countries) {
                if (birthPlace.equals(c.getName())) {                         // label-deki country ve nationality
                    user.setBirthplaceId(c);                                  // Db-da movcud birthP. ve natio. lar arasindan sechilir
                }                                                             // ve user-e set edilir

                if (nationality.equals(c.getNationality())) {
                    user.setNationalityId(c);
                }
            }

            user.setName(name);                                              // qalan melumatlar user-e set edilir
            user.setSurname(surname);
            user.setPhone(phone);
            user.setEmail(email);
            user.setPassword(password);
            user.setProfileDescription(profileDescription);
            user.setAddress(address);
//            user.setAuthorityId(new Authority(1));

            UserDaoInter userDao = Context.instanceUserDao();
            userDao.add(user);                                              // add olunur, evvelki sehifeye qayidilir
            response.sendRedirect("users");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("backUrl",
                "http://" +
                        request.getServerName() +
                        ":" +
                        request.getLocalPort() +
                        request.getContextPath() +
                        "/users");
        request.getRequestDispatcher("user-create.jsp").forward(request, response);
    }
}
