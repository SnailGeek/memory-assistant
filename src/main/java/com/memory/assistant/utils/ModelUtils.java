package com.memory.assistant.utils;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;


public class ModelUtils {
    public static <T, S> T map(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(source, targetClass);
    }


    public static <T, S> List<T> mapList(List<S> sourceList, Class<T> targetItemClass) {
        if (sourceList == null) {
            return null;
        }
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<T> result = new ArrayList<>();
        for (S sourceItem : sourceList) {
            if (sourceItem != null) {
                T resultItem = mapper.map(sourceItem, targetItemClass);
                result.add(resultItem);
            }
        }
        return result;
    }

//    public static <T, S> PageBean<T> mapPage(Page<S> sourcePage, Class<T> targetItemClass) {
//        if (sourcePage == null)
//            return null;
//        PageBean<T> pageDTO = new PageBean<>();
//        //设置当前页数
//        pageDTO.setCurrentPage(sourcePage.getCurrent());
//        //设置数据
//        pageDTO.setRecords(mapList(sourcePage.getRecords(), targetItemClass));
//        //设置每页条数
//        pageDTO.setPageSize(sourcePage.getSize());
//        //设置总条数
//        pageDTO.setTotalCount(sourcePage.getTotal());
//        //设置总页数
//        pageDTO.setTotalPage(sourcePage.getPages());
//
//        return pageDTO;
//    }

    public static <T, S> void assign(S source, T target) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapper.map(source, target);
    }

    public static <T, S> void assignList(List<S> sourceList, List<T> targetList, Class<T> targetItemClass, BiPredicate<S, T> predicate) {
        List<T> additionalList = new ArrayList<T>();
        for (S sourceItem : sourceList) {
            T bingo = matchTarget(sourceItem, targetList, predicate);
            if (bingo != null) {
                assign(sourceItem, bingo);
            } else {
                T newItem = map(sourceItem, targetItemClass);
                additionalList.add(newItem);
            }
        }
        targetList.addAll(additionalList);
    }

    private static <T, S> T matchTarget(S sourceItem, List<T> targetList, BiPredicate<S, T> predicate) {
        for (T targetItem : targetList) {
            if (predicate.test(sourceItem, targetItem)) {
                return targetItem;
            }
        }
        return null;
    }
}