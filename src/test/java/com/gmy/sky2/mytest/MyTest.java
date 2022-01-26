package com.gmy.sky2.mytest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**`
 * @Author guomaoyang
 * @Date 2021/4/23
 */
public class MyTest {

    public int[] twoSum2(int[] nums, int target) {
        int[] ints = new int[2];
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i],i);
        }
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            Integer result = map.get(target - num);
            if(result != null && result != i){
                ints[0] = i;
                ints[1] = result;
                return ints;
            }
        }
        return null;
    }

    public int[] twoSum(int[] nums, int target) {
        int[] ints = new int[2];
        int first = 0;
        int second = 1;
        while(first < nums.length && second< nums.length){
            if(nums[first] + nums[second] == target){
                ints[0] = first;
                ints[1] = second;
                break;
            }
            if(second < nums.length -1){
                second ++;
            }else if(second == nums.length-1){
                first++;
                second = first +1;
            }
        }
        return ints;
    }

    @Test
    void test3(){
        int thehold = 8;
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            list.add(i);
        }
        List<Integer> list1 = list.subList(0, thehold);
        List<Integer> list2 = list.subList(thehold, list.size());

        System.out.println(list1);
        System.out.println(list2);
    }

    @Test
    void test12(){
        TestVO testVO1 = new TestVO(1);
        TestVO testVO2 =testVO1;
        testVO1 = new TestVO(2);
        System.out.println("testVO1："+testVO1);
        System.out.println("testVO2："+testVO2);
    }



    @Test
    void test1(){
        Son son = new Son();
        BigDecimal bigDecimal = new BigDecimal(1);
        double v = bigDecimal.doubleValue();
    }
}
/*
new*
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
*/
class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }}
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        return null;
    }
}