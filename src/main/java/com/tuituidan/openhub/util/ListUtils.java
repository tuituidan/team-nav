package com.tuituidan.openhub.util;

import com.tuituidan.openhub.bean.dto.SortDto;
import com.tuituidan.openhub.bean.entity.ISortEntity;
import com.tuituidan.openhub.bean.vo.TreeData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * ListUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2023/12/3
 */
@UtilityClass
public class ListUtils {

    /**
     * 构建树结构，指定根节点
     *
     * @param dataList dataList
     * @param pid pid
     * @param <T> T
     * @return treeList
     */
    public <T extends TreeData<T>> List<T> buildTree(Collection<T> dataList, String pid) {
        return buildTree(dataList, key -> Objects.equals(pid, key));
    }

    /**
     * 构建树结构，自查找根节点
     *
     * @param dataList dataList
     * @param <T> T
     * @return treeList
     */
    public <T extends TreeData<T>> List<T> buildTree(Collection<T> dataList) {
        Set<String> ids = dataList.stream().map(T::getId).collect(Collectors.toSet());
        return buildTree(dataList, pid -> !ids.contains(pid));
    }

    /**
     * view ui的table拖动排序后端实现
     *
     * @param getFunc getFunc
     * @param before before
     * @param after after
     * @param <T> T
     */
    public <T extends ISortEntity<T>> void changeSort(Supplier<List<T>> getFunc,
            Consumer<List<T>> saveFunc,
            int before, int after) {
        if (before == after) {
            return;
        }
        LinkedList<T> list = new LinkedList<>(getFunc.get());
        if (CollectionUtils.isEmpty(list) || list.size() == 1) {
            return;
        }
        list.add(after, list.remove(before));
        sortListSave(saveFunc, list);
    }

    /**
     * element-ui的el-tree的拖拽排序后端实现
     *
     * @param getFunc getFunc
     * @param saveFunc saveFunc
     * @param sortDto sortDto
     * @param <T> T
     */
    public <T extends ISortEntity<T>> void changeSort(Supplier<List<T>> getFunc,
            Consumer<List<T>> saveFunc,
            SortDto sortDto) {
        LinkedList<T> list = new LinkedList<>(getFunc.get());
        if (CollectionUtils.isEmpty(list) || list.size() == 1) {
            return;
        }
        T draggingNode = list.stream().filter(item -> Objects.equals(item.getId(), sortDto.getDraggingId()))
                .findFirst().orElseThrow(NullPointerException::new);
        T dropNode = list.stream().filter(item -> Objects.equals(item.getId(), sortDto.getDropId()))
                .findFirst().orElseThrow(NullPointerException::new);
        list.remove(draggingNode);
        if ("before".equals(sortDto.getType())) {
            list.add(list.indexOf(dropNode), draggingNode);
        } else {
            list.add(list.indexOf(dropNode) + 1, draggingNode);
        }
        sortListSave(saveFunc, list);
    }

    private <T extends ISortEntity<T>> void sortListSave(Consumer<List<T>> saveFunc, LinkedList<T> list) {
        List<T> updateList = new ArrayList<>();
        int index = 1;
        for (T item : list) {
            if (!Objects.equals(item.getSort(), index)) {
                updateList.add(item.setSort(index));
            }
            index++;
        }
        if (CollectionUtils.isEmpty(updateList)) {
            return;
        }
        saveFunc.accept(updateList);
    }

    /**
     * 构建树结构
     *
     * @param dataList dataList
     * @param predicate predicate
     * @param <T> T
     * @return treeList
     */
    private <T extends TreeData<T>> List<T> buildTree(Collection<T> dataList, Predicate<String> predicate) {
        if (CollectionUtils.isEmpty(dataList)) {
            return Collections.emptyList();
        }
        List<T> treeList = new ArrayList<>();
        Map<String, List<T>> treeMap = dataList.stream()
                .filter(item -> StringUtils.isNotBlank(item.getPid()))
                .collect(Collectors.groupingBy(T::getPid));
        for (T item : dataList) {
            if (predicate.test(item.getPid())) {
                treeList.add(item);
            }
            List<T> children = treeMap.get(item.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                children.sort(Comparator.comparingInt(T::getSort));
            }
            item.setChildren(children);
        }
        treeList.sort(Comparator.comparing(T::getSort));
        return treeList;
    }

}
