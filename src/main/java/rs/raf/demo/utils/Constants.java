package rs.raf.demo.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final String CREATE = "Create";
    public static final String READ = "Read";
    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final String SEARCHV = "SearchV";
    public static final String STARTV = "StartV";
    public static final String STOPV = "StopV";
    public static final String DISCHARGEV = "DischargeV";
    public static final String ADDV = "AddV";
    public static final String REMOVEV = "RemoveV";

    public static final String RUNING_STATUS = "Running";
    public static final String STOPPED_STATUS = "Stopped";
    public static final String DISCHARGING_STATUS = "Discharging";
    public static Tuple<String, Long, PermissionType>[] permissions =
            new Tuple[]{
                    new Tuple<>(CREATE, 1L, PermissionType.CREATE),
                    new Tuple<>(READ, 2L, PermissionType.READ),
                    new Tuple<>(UPDATE, 3L, PermissionType.UPDATE),
                    new Tuple<>(DELETE, 4L, PermissionType.DELETE),
                    new Tuple<>(SEARCHV, 5L, PermissionType.SEARCHV),
                    new Tuple<>(STARTV, 6L, PermissionType.STARTV),
                    new Tuple<>(STOPV, 7L, PermissionType.STOPV),
                    new Tuple<>(DISCHARGEV, 8L, PermissionType.DISCHARGEV),
                    new Tuple<>(ADDV, 9L, PermissionType.ADDV),
                    new Tuple<>(REMOVEV, 10L, PermissionType.REMOVEV)
            };

    public static Map<Long, PermissionType> numberToType = new HashMap<>();
    public static Map<PermissionType, Long> typeToNumber = new HashMap<>();
    public static Map<String, Long> nameToNumber = new HashMap<>();
    public static Map<PermissionType, String> typeToName = new HashMap<>();

    static {
        for (int i = 0; i < Constants.permissions.length; i++) {
            numberToType.put(Constants.permissions[i].getSecond(), Constants.permissions[i].getThird());
        }
        for (Map.Entry<Long, PermissionType> entry : numberToType.entrySet()) {
            typeToNumber.put(entry.getValue(), entry.getKey());
        }
        for (int i = 0; i < Constants.permissions.length; i++) {
            nameToNumber.put(Constants.permissions[i]. getFirst(), Constants.permissions[i].getSecond());
        }
        for (int i = 0; i < Constants.permissions.length; i++) {
            typeToName.put(Constants.permissions[i].getThird(), Constants.permissions[i].getFirst());
        }
    }

    public static Map<VacuumStatus, String> statusToName = Map.of(
            VacuumStatus.STOPPED, STOPPED_STATUS,
            VacuumStatus.RUNNING, RUNING_STATUS,
            VacuumStatus.DISCHARGING, DISCHARGING_STATUS
    );
}
