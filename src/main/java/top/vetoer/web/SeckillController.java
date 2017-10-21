package top.vetoer.web;

import top.vetoer.dto.Exposer;
import top.vetoer.dto.SeckillExecution;
import top.vetoer.dto.SeckillResult;
import top.vetoer.entity.Seckill;
import top.vetoer.enums.SeckillStatEnum;
import top.vetoer.exception.RepeatKillException;
import top.vetoer.exception.SeckillCloseException;
import top.vetoer.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill") //url:/模块/资源/{id}/细分
public class SeckillController {
    //日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model){
        // 获取列表页
        List<Seckill> list =  seckillService.getSeckillList();
        model.addAttribute("list",list);
        // list.jsp+model = ModelAndView
        return "list"; ///WEB-INF/jsp/list.jsp
    }

    @RequestMapping(value = "/{seckillId}/detail",method=RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if(seckillId == null){
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if(seckill==null){
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }
    // ajax json
    @RequestMapping(value = "/{seckillId}/exposer",method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId){
        SeckillResult<Exposer> result;
        try{
            Exposer  exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true,exposer);
        }catch (Exception e){
            logger.error(e.getMessage());
            result = new SeckillResult<Exposer>(false,e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone",required = false) Long phone){
        SeckillResult<SeckillExecution> result;
        // springmvc valid
        if(phone == null){
            return new SeckillResult<SeckillExecution>(false,"未注册");
        }
        try{
            // 存储过程调用
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            return new SeckillResult<SeckillExecution>(true,execution);

        }catch(RepeatKillException e1){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true,execution);

        } catch (SeckillCloseException e2){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(true,execution);

        } catch (Exception e){
            logger.error(e.getMessage());
            SeckillExecution execution = new SeckillExecution(seckillId,SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true,execution);
        }
    }

    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return new SeckillResult<Long>(true,now.getTime());

    }


















}