package com.excilys.computerDatabase.controller.servlet;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.computerDatabase.controller.page.Page;
import com.excilys.computerDatabase.dto.ComputerFormDTO;
import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.mapper.ComputerMapper;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.search.SqlFilter;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.session.Session;

@Controller
@RequestMapping(value = "/dashboard")
public class DashboardController {

	private static final String ROWS_PER_PAGE_PARAM = "displayedRowsPerPage";
	private static final String PAGE_INDEX_PARAM = "pageIndex";
	private static final String TOTAL_COMPUTERS_ATTRIBUTE = "totalComputers";
	private static final String COMPUTER_LIST_ATTRIBUTE = "computers";
	private static final String MAX_PAGES_ATTRIBUTE = "maxPages";
	private static final String SELECTED_COMPUTERS_ATTRIBUTE = "selection";
	private static final String SELECTED_COMPUTER_DELIMITER = ",";
	private static final String SEARCH_PARAM = "search";
	private static final String SORTING_CRITERIA_ATTRIBUTE = "sortCriteria";
	private static final String SORTING_CRITERIA_DELIMITER = ";";
	
	private static final String VIEW_NAME = "dashboard";
	
	Logger logger = LoggerFactory.getLogger(DashboardController.class);

	ComputerService computerService;
	ComputerMapper computerMapper;
	Session session;
	
	public DashboardController(ComputerService computerService, ComputerMapper computerMapper, Session session) {
		this.computerService = computerService;
		this.computerMapper = computerMapper;
		this.session = session;
	}
	
	@GetMapping
	public ModelAndView doGet(@RequestParam(name = PAGE_INDEX_PARAM, defaultValue = Page.DEFAULT_PAGE_INDEX) int pageIndex,
			@RequestParam(name = SEARCH_PARAM, defaultValue = SqlFilter.DEFAULT_SEARCH) String search) {
		return getModelAndView(pageIndex, search);

	}
	
	@PostMapping
	public ModelAndView doPost(@RequestParam(name = PAGE_INDEX_PARAM, defaultValue = Page.DEFAULT_PAGE_INDEX) int pageIndex,
			@RequestParam(name = SEARCH_PARAM, defaultValue = SqlFilter.DEFAULT_SEARCH) String search,
			@RequestParam(name = ROWS_PER_PAGE_PARAM, required = false) Integer rowsPerPage,
			@RequestParam(name = SORTING_CRITERIA_ATTRIBUTE, required = false) String sortingCriterias,
			@RequestParam(name = SELECTED_COMPUTERS_ATTRIBUTE, required = false) String selectedComputersId) {
		if (rowsPerPage != null && rowsPerPage != 0) {
			session.setRowsPerPage(rowsPerPage);
		} else if (sortingCriterias != null) {
			session.setSortingCriteria(sortingCriterias);
		} else if (selectedComputersId != null) {
			deleteComputers(Stream.of(selectedComputersId.split(SELECTED_COMPUTER_DELIMITER)).mapToLong(Long::parseLong).toArray());
		}
		return getModelAndView(pageIndex, search);
	}
	
	private ModelAndView getModelAndView(int pageIndex, String search) {
		ModelAndView mv = new ModelAndView(VIEW_NAME);

		int rowsPerPage = session.getRowsPerPage() != 0 ? session.getRowsPerPage() : Integer.parseInt(Page.DEFAULT_ROWS_PER_PAGE);
		String[] parsedSortCriterias = parseOrderBy(session.getSortingCriteria());
		SqlFilter filter = new SqlFilter(parsedSortCriterias[0], parsedSortCriterias[1], search);
		
		List<Computer> computers = computerService.getComputers((pageIndex - 1) * rowsPerPage, rowsPerPage, filter);
		List<ComputerFormDTO> dtos = computers.stream().map(c -> computerMapper.toComputerFormDTO(c)).collect(Collectors.toList());
		
		Page<ComputerFormDTO> page = new Page<ComputerFormDTO>(dtos);
		
		int totalComputers = computerService.getComputerCount(search);
		
		mv.getModel().put(COMPUTER_LIST_ATTRIBUTE, page.getContent());
		mv.getModel().put(TOTAL_COMPUTERS_ATTRIBUTE, totalComputers);
		mv.getModel().put(MAX_PAGES_ATTRIBUTE, (int) Math.ceil(totalComputers / rowsPerPage));
		mv.getModel().put(PAGE_INDEX_PARAM, pageIndex);

		return mv;
	}
	
	private void deleteComputers(long[] computersId) {
		for (long id : computersId) {
			try {
				computerService.deleteComputer(id);
			} catch (FailedSQLRequestException e) {
				logger.warn(e.getMessage());
			}
		}
	}
	
	private String[] parseOrderBy(String criterias) {
		String[] result = new String[2];
		if (criterias != null) {
			result =  criterias.split(SORTING_CRITERIA_DELIMITER);
		}
		return result;
	}
}
