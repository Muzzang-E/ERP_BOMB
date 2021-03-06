package com.greedy.erp_bomb.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greedy.erp_bomb.inventory.model.dto.CompanyDTO;
import com.greedy.erp_bomb.inventory.model.dto.IceCreamDTO;
import com.greedy.erp_bomb.inventory.model.dto.InventoryDTO;
import com.greedy.erp_bomb.inventory.model.service.InventoryService;

@Controller
@RequestMapping("/inventory")
public class InventoryController {
	
	private InventoryService inventoryService;
	
	@Autowired
	public InventoryController(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	@GetMapping("/inventory")
	public ModelAndView findInvenList(ModelAndView mv) {
		List<InventoryDTO> invenList = inventoryService.findInvenList();
		
		mv.addObject("inventoryList", invenList);
		mv.setViewName("inventory/inventory");
		return mv;
	}
	
	@GetMapping(value = "/company", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public List<CompanyDTO> findCompanyList() {
		return inventoryService.findCompanyList();
	}
	
	@GetMapping(value = "/icecream", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public List<IceCreamDTO> findIcecreamList() {
		return inventoryService.findIcecreamList();
	}
	
	@PostMapping("/regist")
	public ModelAndView registInven(ModelAndView mv, @RequestParam int companyCode,  @RequestParam int icecreamCode,
			                        @RequestParam int invenRemainStock, RedirectAttributes rttr) {
		
		/* 회사 테이블 */
		CompanyDTO com = new CompanyDTO();
		com.setSerialNo(companyCode);
		
		/* 아이스크림 테이블 */
		IceCreamDTO ice = new IceCreamDTO();
		ice.setNo(icecreamCode);
		
		/* 재고 관리 테이블 */
		InventoryDTO inven = new InventoryDTO();
		inven.setCompany(com);
		inven.setIceCream(ice);
		inven.setInvenRemainStock(invenRemainStock);
		
		InventoryDTO insertInven = inventoryService.findInsertInven(inven);
		
		if (insertInven == null) {
			inventoryService.registInven(inven);
		} else {
			rttr.addFlashAttribute("errorMessage", "해당 회사에 해당 아이스크림은 이미 존재합니다");
		}
		
		mv.setViewName("redirect:/inventory/inventory");
		
		return mv;
		
	}
	
	
}
