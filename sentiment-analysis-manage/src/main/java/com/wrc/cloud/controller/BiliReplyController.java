package com.wrc.cloud.controller;

import com.wrc.cloud.DO.BiliReplyDO;
import com.wrc.cloud.DTO.BiliReplyDTO;

import com.wrc.cloud.PO.AnalysisPO;
import com.wrc.cloud.PO.BiliReplyPO;
import com.wrc.cloud.entities.ResponseResult;
import com.wrc.cloud.service.BiliReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;


/**
 * (TBiliReply)表控制层
 *
 * @author makejava
 * @since 2023-01-20 09:23:28
 */
@RestController
@RequestMapping("/api/bili")
@Slf4j
public class BiliReplyController {

    @Resource
    private BiliReplyService biliReplyService;


    /**
     * 返回已有数据量
     * */
    @GetMapping("/reply/count")
    public ResponseResult<Long> getCount(){
        return ResponseResult.success(biliReplyService.getCount(new BiliReplyPO()));
    }

    /**
     * 新增数据
     */
    @PostMapping("/reply")
    public ResponseEntity<Integer> add(@RequestBody BiliReplyDTO replyDTO) {
//        log.info(replyDTO.toString());
        return ResponseEntity.ok(this.biliReplyService.insert(new BiliReplyDO(replyDTO)));
    }

    /**
     * 编辑数据
     */
    @PutMapping("/reply")
    public ResponseEntity<Integer> edit(@RequestBody BiliReplyDTO replyDTO) {
        return ResponseEntity.ok(this.biliReplyService.update(new BiliReplyDO(replyDTO)));
    }

    /**
     * 删除数据
     */
    @DeleteMapping("/reply")
    public ResponseEntity<Integer> deleteById(BigInteger id) {
        return ResponseEntity.ok(this.biliReplyService.deleteById(id));
    }


    /**
     * 分页查询
     *
     * @param replyDTO  筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
//    @GetMapping
//    public ResponseEntity<Page<BiliReplyDTO>> queryByPage(BiliReplyDTO replyDTO, PageRequest pageRequest) {
//        BiliReplyDO replyDO = new BiliReplyDO(replyDTO);
//        this.biliReplyService.queryByPage(replyDO, pageRequest);
//        return ResponseEntity.ok();
//    }

    /**
     * 通过主键查询单条数据
     *
     * @  主键
     * @return 单条数据
     */
//    @GetMapping("/{id}")
//    public ResponseEntity<BiliReplyDTO> queryById(@PathVariable("id") BigInteger id) {
//        return ResponseEntity.ok(this.biliReplyService.queryById(id));
//    }

    @GetMapping("/analysis")
    public ResponseResult getVideoAnalysis(){
        return new ResponseResult("not finished");
    }


    @GetMapping("/analysis/count")
    public ResponseResult<Long> getBiliAnalysisCount(@RequestParam("id") BigInteger id,
                                                     @RequestParam("analysis") String analysis){
        AnalysisPO condition = new AnalysisPO();
        condition.setId(id);
        condition.setAnalysis(analysis);
        Long analysisCount = biliReplyService.getAnalysisCount(condition);
        return ResponseResult.success(analysisCount);
    }


}

