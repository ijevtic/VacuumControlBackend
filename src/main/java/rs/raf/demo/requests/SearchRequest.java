package rs.raf.demo.requests;

import lombok.Data;

import java.util.List;

@Data
public class SearchRequest {
    private String name;
    private List<String> status;
    private Long dateFrom;
    private Long dateTo;
}
