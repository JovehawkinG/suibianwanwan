package logic;

import java.util.Arrays;
import java.util.PriorityQueue;

// 253
// 没做出来
public class S_253_SMinMeetingRooms {
    public int minMeetingRooms(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        PriorityQueue<Integer> rooms = new PriorityQueue<>();
        for (int[] meeting : intervals) {
            if (!rooms.isEmpty() && rooms.peek() <= meeting[0]) {
                rooms.poll();
            }
            rooms.offer(meeting[1]);
        }
        return rooms.size();
    }
}
