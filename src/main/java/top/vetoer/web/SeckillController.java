package top.vetoer.web;

import com.google.code.kaptcha.Producer;
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
import top.vetoer.utils.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill") //url:/模块/资源/{id}/细分
public class SeckillController {
    //日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;


    @RequestMapping(value = {"/list?page={pageNow}", "/list"}, method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) {
        // 实现分页功能
        int totalProduct = seckillService.queryTotal();
        Page page = null;
        List<Seckill> list = null;
        if (totalProduct == 0) {           // 商品列表为空,返回空
            model.addAttribute("listEmpty", "查询列表为空,暂时没有可秒杀的商品");
        } else if (totalProduct > 0 && totalProduct < 7) {   // 商品数量小于一页
            list = seckillService.getSeckillList();
        } else if (totalProduct > 7) {                           // 分页处理
            String pageCount = request.getParameter("page");
            int pageNow = 1;
            if (pageCount != null) {
                pageNow = Integer.parseInt(pageCount);
            }
            page = new Page(totalProduct, pageNow);
            list = seckillService.queryByPage(page);
        }
        model.addAttribute("page", page);
        model.addAttribute("list", list);
        model.addAttribute("type", "秒杀");
        // list.jsp+model = ModelAndView
        return "list"; ///WEB-INF/jsp/list.jsp
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    // ajax json
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @SessionAttribute(value = "userId", required = false) long userId) {
        SeckillResult<SeckillExecution> result;
        // springmvc valid
        if (userId == 0) {
            return new SeckillResult<SeckillExecution>(false, "未登录");
        }
        try {
            // 存储过程调用
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, userId, md5);
            return new SeckillResult<SeckillExecution>(true, execution);

        } catch (RepeatKillException e1) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true, execution);

        } catch (SeckillCloseException e2) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(true, execution);

        } catch (Exception e) {
            logger.error(e.getMessage());
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, execution);
        }
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public String queryList(Model model, String orderName) {
        // 获取查询列表
        List<Seckill> list = seckillService.queryByName(orderName);
        if (list != null && list.size() > 0) {
            model.addAttribute("list", list);
        } else {
            model.addAttribute("listEmpty", "查询列表为空");
        }
        model.addAttribute("type", "查询");
        return "list";
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());

    }


}
