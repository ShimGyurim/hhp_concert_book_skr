package io.hhplus.cleancode.controller;


import io.hhplus.cleancode.dto.ItemDto;
import io.hhplus.cleancode.dto.ResponseDto;
import io.hhplus.cleancode.service.QuickService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class QuickController {

    @Autowired
    private QuickService quickService;

    @GetMapping("/dummy")
    public String dummy() {
        log.info("dummy");
        return "{}";
    }
    @GetMapping("/dummy2")
    public String dummy2() {
        return "dummy2";
    }

    @GetMapping("/member")
    public String getMember(@RequestParam("empNo") String empNo,
                            @RequestParam("year") int year){
        log.info("empNo: {}",empNo);
        log.info("year: {}",year);
        return "ok";
    }
    @GetMapping("/company/{id}")
    public String getCompany(@PathVariable("id") String id){
        log.info("id: {}",id);
        return "ok";
    }

    @PostMapping("/item") //등록
    public ResponseDto registerItem(@RequestBody ItemDto item) { //post는 requestBody 로 body 를 보내서 함
        log.info("item: {}",item);

//        QuickService quickService = new QuickService();
        boolean b = quickService.registerItem(item);

        if(b==true) {
            ResponseDto responseDto = new ResponseDto();
            responseDto.setMessage("ok");
            return responseDto;
        }
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("fail");
        return responseDto;

    }

    @GetMapping("/item")
    public ItemDto getItem(@RequestParam("id") String id) {
        ItemDto res = quickService.getItemById(id);
        log.info("id: {}",res);
        return res;
    }

}
