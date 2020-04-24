package cn.shenjunjie.booking.repo;

import cn.shenjunjie.booking.dao.MessageMapper;
import cn.shenjunjie.booking.dto.request.GetMessageRequest;
import cn.shenjunjie.booking.entity.Message;
import cn.shenjunjie.booking.entity.MessageExample;
import cn.shenjunjie.booking.utils.DateUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:51
 */
@Repository
public class MessageRepo {

    @Resource
    private MessageMapper messageMapper;

    public Message selectById(Long id) {
        return messageMapper.selectByPrimaryKey(id);
    }

    public Page<Message> selectByRequestAndTo(GetMessageRequest request, Long to) {
        return selectByRequestAndTo(request, to, true);
    }

    public Page<Message> selectByRequestAndTo(GetMessageRequest request, Long to, boolean pageAble) {
        if (pageAble) {
            PageHelper.startPage(request.getPageCurrent(), request.getPageSize());
        }
        MessageExample example = new MessageExample();
        MessageExample.Criteria criteria = example.createCriteria();
        if (to != null) {
            criteria.andToEqualTo(to);
        }
        if (StringUtils.isNotBlank(request.getCreatedAtFrom()) && StringUtils.isNotBlank(request.getCreatedAtTo())) {
            Date atFrom = DateUtil.formatDateByString(request.getCreatedAtFrom());
            Date atTo = DateUtil.formatDateByString(request.getCreatedAtTo());
            criteria.andCreatedatBetween(atFrom,atTo);
        }
        return (Page<Message>) messageMapper.selectByExampleWithBLOBs(example);
    }


    public void insertByRecord(Message record) {
        messageMapper.insertSelective(record);
    }

    public void updateByIdAndReaded(Long id, Integer readed) {
        Message record = new Message();
        record.setId(id);
        record.setReaded(readed);
        messageMapper.updateByPrimaryKeySelective(record);
    }

    public void deleteById(Long id) {
        messageMapper.deleteByPrimaryKey(id);
    }

    public Long countByReaded(Integer readed, Long to) {
        MessageExample example = new MessageExample();
        MessageExample.Criteria criteria = example.createCriteria();
        criteria.andReadedEqualTo(readed);
        criteria.andToEqualTo(to);
        return messageMapper.countByExample(example);
    }

    public List<Message> selectByToAndReaded(Long to, int readed) {
        MessageExample example = new MessageExample();
        MessageExample.Criteria criteria = example.createCriteria();
        criteria.andToEqualTo(to);
        criteria.andReadedEqualTo(readed);
        return messageMapper.selectByExampleWithBLOBs(example);
    }
}
