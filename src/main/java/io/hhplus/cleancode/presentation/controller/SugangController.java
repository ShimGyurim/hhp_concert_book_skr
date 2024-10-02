package io.hhplus.cleancode.presentation.controller;


import io.hhplus.cleancode.domain.dto.SugangDto;
import io.hhplus.cleancode.domain.service.SugangService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/sugang")
public class SugangController {

    @Autowired
    SugangService sugangService;

    @GetMapping("/insert/{studentId}/{sugangId}/{availNum}/{sugangName}/{classDate}")
    public String insert(@PathVariable("studentId") Long studentId,
                        @PathVariable("sugangId") Long sugangId,
                         @PathVariable("availNum") Long availNum,
                        @PathVariable("sugangName") String sugangName,
                        @PathVariable("classDate") String classDate) {
        sugangService.insert(new SugangDto(sugangId,studentId,availNum,classDate,sugangName));
        return null;
    }

    @GetMapping("/apply/{studentId}/{sugangId}/{classDate}")
    public String apply(@PathVariable("studentId") Long studentId,
                        @PathVariable("sugangId") Long sugangId,
                        @PathVariable("classDate") String classDate) {
        sugangService.apply(new SugangDto(sugangId,studentId,0,classDate,null));
        return null;
    }

    @GetMapping("/getClassAvail/{classDate}")
    public List<SugangDto> getClassAvail(@PathVariable("classDate") String classDate) {
        List<SugangDto> sugangInsertDtoList = sugangService.getClassAvail(new SugangDto(0L,0L,0L,classDate,""));
        return sugangInsertDtoList ;
    }

    @GetMapping("/getUserClassApply/{studentId}")
    public List<SugangDto> getUserClassApply(@PathVariable("studentId") Long studentId) {
        List<SugangDto> sugangInsertDtoList = sugangService.getClassApplyHistory(new SugangDto(0L,studentId,0L,"",""));
        return sugangInsertDtoList;
    }
//    @Autowired
//    private QuickService quickService;

    //        List<PointHistory> object = pointHistoryTable.selectAllBystudentId(id);

    //특강 신청 : studentId , 강의id , 날짜 (스트링8자)
    //특강신청완료 : 조회 : 특정 studentId => json 배열로 리턴 (날짜,특강id,특강명)
    //특강신청가능 : 조회 : 날짜별로 현재 신청가능 특강 목록 => json 배열로 (날짜,특강id,특강명) 

//    @PostMapping("/item") //등록
//    public ResponseDto registerItem(@RequestBody ItemDto item) { //post는 requestBody 로 body 를 보내서 함
//        log.info("item: {}",item);
//
////        QuickService quickService = new QuickService();
//        boolean b = quickService.registerItem(item);
//
//        if(b==true) {
//            ResponseDto responseDto = new ResponseDto();
//            responseDto.setMessage("ok");
//            return responseDto;
//        }
//        ResponseDto responseDto = new ResponseDto();
//        responseDto.setMessage("fail");
//        return responseDto;
//    }

//    @GetMapping("/item")
//    public ItemDto getItem(@RequestParam("id") String id) {
//        ItemDto res = quickService.getItemById(id);
//        log.info("id: {}",res);
//        return res;
//    }

//    @GetMapping("/company/{id}")
//    public String getCompany(@PathVariable("id") String id){
//        log.info("id: {}",id);
//        return "ok";
//    }

}
