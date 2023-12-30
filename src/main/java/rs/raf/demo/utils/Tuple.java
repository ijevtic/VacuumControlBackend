package rs.raf.demo.utils;

import lombok.Data;

@Data
public class Tuple<A,B,C>{
    private A first;
    private B second;
    private C third;

    public Tuple(A first, B second, C third){
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
