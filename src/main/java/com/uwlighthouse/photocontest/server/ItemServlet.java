package com.uwlighthouse.photocontest.server;

import static com.uwlighthouse.photocontest.server.ServerUtil.getCurrentWeekNumber;
import static com.uwlighthouse.photocontest.server.ServerUtil.getNextWeekNumber;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.uwlighthouse.photocontest.daos.ItemDao;
import com.uwlighthouse.photocontest.databaseobjects.Item;

public class ItemServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6385271514880619229L;

	/**
	 * Responds with the current week and next week's item in the form:
	 * {thisWeek: "Item1", nextWeek: "Item2"}
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Item thisWeek = new ItemDao().findByWeek(getCurrentWeekNumber());
		Item nextWeek = new ItemDao().findByWeek(getNextWeekNumber());

		response.setContentType("application/json");
		response.getWriter().print(new Gson().toJson(new ItemDto(thisWeek, nextWeek)));
	}
}
