import com.outs.utils.kotlin.d
import com.outs.utils.kotlin.reduce
import com.outs.utils.kotlin.toLogString
import kotlin.test.Test

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/3/23 16:39
 * desc:
 */
class Leecode {

    class TwoSum {

        fun twoSum(nums: IntArray, target: Int): IntArray {
            val map = LinkedHashMap<Int, Int>()
            for (i in nums.indices) {
                val curIndex = i
                val curValue = nums[i]
                val elseValue = target - curValue
                if (map.containsKey(elseValue)) {
                    val elseIndex = map[elseValue]
                    if (null != elseIndex)
                        return intArrayOf(elseIndex, curIndex)
                }
                map[curValue] = curIndex
            }
            return intArrayOf(0, 0)
        }

        @Test
        fun testTwoSum() {
            twoSum(nums = intArrayOf(2, 7, 11, 15), target = 9).d()
        }

    }

    class AddTwoNumbers {

        /**
         * 给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。

        请你将两个数相加，并以相同形式返回一个表示和的链表。

        你可以假设除了数字 0 之外，这两个数都不会以 0 开头。

         

        示例 1：


        输入：l1 = [2,4,3], l2 = [5,6,4]
        输出：[7,0,8]
        解释：342 + 465 = 807.
        示例 2：

        输入：l1 = [0], l2 = [0]
        输出：[0]
        示例 3：

        输入：l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
        输出：[8,9,9,9,0,0,0,1]
         

        提示：

        每个链表中的节点数在范围 [1, 100] 内
        0 <= Node.val <= 9
        题目数据保证列表表示的数字不含前导零

        来源：力扣（LeetCode）
        链接：https://leetcode-cn.com/problems/add-two-numbers
        著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
         */
        /**
         * Example:
         * var li = ListNode(5)
         * var v = li.`val`
         * Definition for singly-linked list.
         * class ListNode(var `val`: Int) {
         *     var next: ListNode? = null
         * }
         */
        class ListNode(var `val`: Int) {
            var next: ListNode? = null
        }

        @Test
        fun testAddTwoNumbers() {
            fun ListNode.toArray(): IntArray {
                val ret = ArrayList<Int>()
                var cur: ListNode? = this
                while (null != cur) {
                    ret.add(cur.`val`)
                    cur = cur.next
                }
                return ret.toIntArray()
            }

            fun IntArray.toNodes(): ListNode? = ListNode(0)
                .also {
                    map(::ListNode).reduce(it) { cur, next ->
                        cur.next = next
                        next
                    }
                }
                .next

            val l1 = intArrayOf(9, 9, 9, 9, 9, 9, 9).toNodes()
            val l2 = intArrayOf(9, 9, 9, 9).toNodes()
            l1?.toArray().d()
            l2?.toArray().d()
            addTwoNumbers(l1, l2)?.toArray().d()
        }

        fun addTwoNumbers(l1: ListNode?, l2: ListNode?): ListNode? {
            var carry = 0
            var node1 = l1
            var node2 = l2
            var current = ListNode(0)
            val ret: ListNode = current
            while (null != node1 || null != node2) {
                var num = carry
                if (null != node1) {
                    num += node1.`val`
                    node1 = node1.next
                }
                if (null != node2) {
                    num += node2.`val`
                    node2 = node2.next
                }
                carry = num / 10
                num %= 10
                val next = ListNode(num)
                current.next = next
                current = next
            }
            if (0 != carry) {
                current.next = ListNode(carry)
            }
            return ret.next
        }

    }

    class LengthOfLongestSubstring {

        @org.junit.Test
        fun testLengthOfLongestSubstring() {
            lengthOfLongestSubstring("abcabcbb").d()
            lengthOfLongestSubstring("bbbbb").d()
            lengthOfLongestSubstring("pwwkew").d()
            lengthOfLongestSubstring(" ").d()
            lengthOfLongestSubstring("").d()
            lengthOfLongestSubstring("au").d()
        }

        fun lengthOfLongestSubstring(s: String): Int {
            "输入字符串： $s".d()
            if (s.isEmpty()) return 0
            if (1 == s.length) return 1
            fun IntRange.length() = last - first + 1
            val ranges = HashSet<IntRange>()
            val set = HashSet<Char>()
            var last: IntRange? = null
            var i = 0
            while (i < s.length) {
                set.add(s[i])
                val rangeStart: Int
                val loopStart: Int
                if (null != last && 1 != last.length()) {
                    rangeStart = last.first + 1
                    loopStart = last.last + 1
                } else {
                    rangeStart = i
                    loopStart = i + 1
                }
                for (rangeEnd in loopStart..s.length) {
                    if (rangeEnd == s.length || !set.add(s[rangeEnd])) {
                        val range = rangeStart until rangeEnd
                        last = range
                        ranges.add(range)
                        break
                    }
                }
                set.remove(s[i])
                i++
            }
//    ranges.forEach { "子字符串： ${s.substring(it)} 长度：${it.length()}".d() }
//    return ranges.map { it.length() }.max() ?: 0
            return ranges.maxOfOrNull { it.length() } ?: 1
        }

        fun lengthOfLongestSubstring2(s: String): Int {
            val set = HashSet<Char>()
            val n = s.length
            var rk = -1
            var ans = 0
            for (i in s.indices) {
                if (0 != i) {
                    set.remove(s[i - 1])
                }
                while (rk + 1 < n && set.add(s[rk + 1])) {
                    ++rk
                }
                ans = ans.coerceAtLeast(rk - i + 1)
            }
            return ans
        }

    }

    class FindMedianSortedArrays {

        @org.junit.Test
        fun testFindMedianSortedArrays() {
            fun p(a1: IntArray, a2: IntArray) {
                val median = findMedianSortedArrays3(a1, a2)
                "\n数组1：${a1.toLogString()}\n数组2：${a2.toLogString()}\n中位数：{${median}}\n\n".d()
            }
            p(intArrayOf(1, 3), intArrayOf(2))
            p(intArrayOf(1, 2), intArrayOf(3, 4))
            p(intArrayOf(1, 2, 9, 48), intArrayOf(3, 4, 6))
            p(intArrayOf(), intArrayOf(2, 3))
            p(intArrayOf(1, 2), intArrayOf(1, 2, 3))
        }

        fun findMedianSortedArrays(nums1: IntArray, nums2: IntArray): Double {
            val length1 = nums1.size
            val length2 = nums2.size
            var index1 = 0
            var index2 = 0
            var value1 = Int.MIN_VALUE
            var value2 = Int.MIN_VALUE
            var lastFrom1 = false
            fun next(i: Int): Int {
                if (index1 == length1) {
                    return nums2[index2++]
                }
                if (index2 == length2) {
                    return nums1[index1++]
                }
                if (i == 0) {
                    value1 = nums1[index1]
                    value2 = nums2[index2]
                } else if (lastFrom1) {
                    value1 = nums1[index1]
                } else {
                    value2 = nums2[index2]
                }
                return if (value1 > value2) {
                    index2++
                    lastFrom1 = false
                    value2
                } else {
                    index1++
                    lastFrom1 = true
                    value1
                }
            }

            fun repeat(times: Int) {
                for (index in 0 until times) {
                    next(index)
                }
            }

            val count1 = nums1.size
            val count2 = nums2.size
            val total = count1 + count2
            val medianIndex = total / 2
            val isEvenNumber = 0 == total % 2
            return if (isEvenNumber) {
                val repeatCount = medianIndex - 1
                if (repeatCount > 0) repeat(repeatCount)
                val n1 = next(repeatCount)
                val n2 = next(repeatCount + 1)
                return n1.plus(n2).toDouble().div(2)
            } else {
                if (medianIndex > 0) repeat(medianIndex)
                next(medianIndex).toDouble()
            }
        }


        fun findMedianSortedArrays2(nums1: IntArray, nums2: IntArray): Double {
            fun IntArray.median(): Double {
                if (isEmpty()) return 0.0
                if (1 == size) return get(0).toDouble()
                val median = size / 2
                val isEvenNumber = 0 == size % 2
                return if (isEvenNumber) {
                    (get(median) + get(median - 1)) / 2.0
                } else {
                    get(median).toDouble()
                }
            }

            fun getKFrom2Array(a1: IntArray, a2: IntArray, k: Int): Int {
                var index1 = 0
                var index2 = 0
                var indexK = k
                while (true) {
                    if (index1 == a1.size) return a2[index2 + indexK - 1]
                    if (index2 == a2.size) return a1[index1 + indexK - 1]
                    if (1 == indexK) {
                        return minOf(a1[index1], a2[index2])
                    }
                    val subK = indexK / 2 - 1
                    val count1 = a1.size - index1
                    val count2 = a2.size - index2
                    val i =
                        if (count1 <= subK) count1 - 1 else if (count2 <= subK) count2 - 1 else subK
                    if (a1[index1 + i] > a2[index2 + i]) {
                        index2 += i + 1
                    } else {
                        index1 += i + 1
                    }
                    indexK -= i + 1
                }
            }

            val count1 = nums1.size
            val count2 = nums2.size
            if (0 == count1) {
                return nums2.median()
            } else if (0 == count2) {
                return nums1.median()
            }
            val total = count1 + count2
            val median = total / 2
            val isEvenNumber = 0 == total % 2
            return if (isEvenNumber) {
                val k1 = getKFrom2Array(nums1, nums2, median)
                val k2 = getKFrom2Array(nums1, nums2, median + 1)
                k1.plus(k2).div(2.0)
            } else {
                getKFrom2Array(nums1, nums2, median + 1).toDouble()
            }

        }

        fun findMedianSortedArrays3(nums1: IntArray, nums2: IntArray): Double {
            if (nums1.size > nums2.size) return findMedianSortedArrays3(nums2, nums1)

            val m = nums1.size
            val n = nums2.size
            var left = 0
            var right = m
            var median1 = 0
            var median2 = 0

            while (left <= right) {
                val i = (left + right) / 2
                val j = (m + n + 1) / 2 - i

                val numsIm1 = if (0 == i) Int.MIN_VALUE else nums1[i - 1]
                val numsI = if (m == i) Int.MAX_VALUE else nums1[i]
                val numsJm1 = if (0 == j) Int.MIN_VALUE else nums2[j - 1]
                val numsJ = if (n == j) Int.MAX_VALUE else nums2[j]

//        fun IntArray.dByI(i: Int): String {
//            if (0 == i) return "${toLogString()}    |    []"
//            if (size <= i) return "[]    |    ${toLogString()}"
//            val list = toList()
//            return "${list.subList(0, i).toLogString()}    |    ${list.subList(i, size).toLogString()}"
//        }
//        """
//            i=$i, array1: ${nums1.dByI(i)}
//            j=$j, array2: ${nums2.dByI(j)}
//            nums1[i-1]=$numsIm1, nums1[i]=$numsI
//            nums2[j-1]=$numsJm1, nums2[j]=$numsJ
//        """.d()
                if (numsIm1 <= numsJ) {
                    median1 = maxOf(numsIm1, numsJm1)
                    median2 = minOf(numsI, numsJ)
                    left = i + 1
                } else {
                    right = i - 1
                }
            }

            return if (0 == (m + n) % 2) (median1 + median2) / 2.0 else median1.toDouble()
        }

    }

    class LongestPalindrome {

        @org.junit.Test
        fun testLongestPalindrome() {
            fun test(input: String) {
                val output = longestPalindrome(input)
                "\n\t输入：s = $input\n\t输出：$output".d()
            }
            test("babad")
            test("cbbd")
        }

        fun longestPalindrome(s: String): String {
            if (s.length in 0..1) return s

            fun String.isPalindrome(): Boolean = (0 until length / 2)
                .all { i -> get(i) == get(length - 1 - i) }

            fun IntRange.isPalindrome(): Boolean = s.substring(this).isPalindrome()

//    val palindromes = ArrayList<IntRange>()
            var ret: IntRange? = null

            for (start in s.indices) {
                for (end in (s.length - 1).downTo(start + 1)) {
                    if (s[start] == s[end]) {
                        (start..end)
                            .takeIf(IntRange::isPalindrome)
                            ?.takeIf { null == ret || (it.last - it.first > ret!!.last - ret!!.first) }
                            ?.also { ret = it }
                    }
                }
            }

//    palindromes.map(s::substring).toLogString().d()

//    return palindromes.maxByOrNull { it.last - it.first }
//        ?.let(s::substring)
//        ?: s.substring(0, 1)

            return ret?.let(s::substring) ?: s.substring(0, 1)
        }

        fun longestPalindrome2(s: String): String {
            val len = s.length
            if (len < 2) return s

            var maxLen = 1
            var begin = 0
            val dp: Array<BooleanArray> = Array(len) { BooleanArray(len) }

            for (i in 0 until len) {
                dp[i][i] = true
            }

            val chars = s.toCharArray()
            for (L in 2..len) {
                for (i in 0 until len) {
                    val j = L + i - 1
                    if (j >= len) break
                    dp[i][j] =
                        if (chars[i] != chars[j]) false else if (j - i < 3) true else dp[i + 1][j - 1]
                    if (dp[i][j] && j - i + 1 > maxLen) {
                        maxLen = j - i + 1
                        begin = i
                    }
                }
            }

            return s.substring(begin, begin + maxLen)
        }
    }

}