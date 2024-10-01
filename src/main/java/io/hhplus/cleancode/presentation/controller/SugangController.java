package io.hhplus.cleancode.presentation.controller;


import io.hhplus.cleancode.application.dto.SugangRecordDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/sugang")
public class SugangController {

    @GetMapping("/apply/{userId}/{classId}")
    public String apply(@PathVariable("userId") String userId,
                        @PathVariable("classId") String classId) {
        return null;
    }

    @GetMapping("/getClassAvail/{classDate}")
    public List<SugangRecordDto> getClassAvail(@PathVariable("classDate") String classDate) {
        return null;
    }

    @GetMapping("/getUserClassApply/{userId}")
    public List<SugangRecordDto> getUserClassApply(@PathVariable("userId") String userId) {
        return null;
    }
//    @Autowired
//    private QuickService quickService;

    //        List<PointHistory> object = pointHistoryTable.selectAllByUserId(id);

    //특강 신청 : userid , 강의id , 날짜 (스트링8자)
    //특강신청완료 : 조회 : 특정 userid => json 배열로 리턴 (날짜,특강id,특강명)
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
