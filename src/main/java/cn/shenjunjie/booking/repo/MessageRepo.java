package cn.shenjunjie.booking.repo;

import cn.shenjunjie.booking.dao.MessageMapper;
import cn.shenjunjie.booking.entity.Message;
import cn.shenjunjie.booking.entity.MessageExample;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:51
 */
@Repository
public class MessageRepo {

    @Resource
    private MessageMapper messageMapper;

    public Message selectById(Long id){
        return messageMapper.selectByPrimaryKey(id);
    }

    public Page<Message> selectByGetMessagesRequest() {
        return selectByGetMessagesRequest(true);
    }

    public Page<Message> selectByGetMessagesRequest(boolean pageAble) {
//        if (pageAble) {
//            PageHelper.startPage(request.getPageCurrent(), request.getPageSize());
//        }
        MessageExample example = new MessageExample();
        MessageExample.Criteria criteria = example.createCriteria();
//        if (!StringUtils.isEmpty(request.getName())) {
//            criteria.andNameLike("%".concat(request.getName()).concat("%"));
//        }
//        if (!StringUtils.isEmpty(request.getIsbn())) {
//            criteria.andIsbnLike("%".concat(request.getIsbn()).concat("%"));
//        }
//        if (!StringUtils.isEmpty(request.getAuthor())) {
//            criteria.andAuthorLike("%".concat(request.getAuthor()).concat("%"));
//        }
//        if (!StringUtils.isEmpty(request.getPress())) {
//            criteria.andPressLike("%".concat(request.getPress()).concat("%"));
//        }
        return (Page<Message>) messageMapper.selectByExample(example);
    }


    public void insertByRecord(Message record) {
        messageMapper.insertSelective(record);
    }

    public void deleteById(Long id){
        messageMapper.deleteByPrimaryKey(id);
    }
}
