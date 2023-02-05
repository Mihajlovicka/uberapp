package com.example.demo.service;

import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.fakeBank.*;
import com.example.demo.model.*;
import com.example.demo.repository.DriveRepository;
import com.example.demo.repository.DriversRepository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class DriveServiceTestDoingDrive {

    @Mock
    private UserService userService;

    @Mock
    private DriveRepository driveRepository;
    @Mock
    private DriversRepository driversRepository;

    @Mock
    private BankTransactionRepository bankRepository;

    @Mock
    private BankService bankService;

    @Mock
    private RideSimulationService rideSimulationService;
    @Spy
    @InjectMocks
    private DriveService driveService;

    private BankTransaction bankTransaction ;

    private User user1;
    private User user2;
    private User user3;
    private User user4;

    private Drive drive;
    private List<Drive> drives;

    private Set<Passenger> passengers;

    private Passenger passenger1;
    private Passenger passenger2;

    private Address address1;
    private Car car;

    private ClientsAccount client1;
    private ClientsAccount client2;
    private ClientsAccount client3;

    private DriversAccount driver;

    private String driveJSON;

    @BeforeEach
    public void setters(){
        drives = new ArrayList<Drive>();
        user1 = new User(1L, "pera@gmail.com", "Petar", "Petrovic", "pera@gmail.com", "$2a$10$rXb3n7ggvOSi6OPcExZWTO.0Zp2LEW.RCOfoKTLkcMdxR79UO9HGy", Status.ACTIVE, new Role());
        user1.getRole().setName("ROLE_CLIENT");

        user2 = new User(2L, "zika@gmail.com", "Zivan", "Raosavljevic", "zika@gmail.com", "$2a$10$rXb3n7ggvOSi6OPcExZWTO.0Zp2LEW.RCOfoKTLkcMdxR79UO9HGy", Status.ACTIVE, new Role());
        user2.getRole().setName("ROLE_DRIVER");

        user3 = new User(1L, "mika@gmail.com", "Mikica", "Mikic", "mika@gmail.com", "$2a$10$rXb3n7ggvOSi6OPcExZWTO.0Zp2LEW.RCOfoKTLkcMdxR79UO9HGy", Status.ACTIVE, new Role());
        user3.getRole().setName("ROLE_CLIENT");

        user4 = new User(1L, "pero@gmail.com", "Pero", "Petrovic", "pero@gmail.com", "$2a$10$rXb3n7ggvOSi6OPcExZWTO.0Zp2LEW.RCOfoKTLkcMdxR79UO9HGy", Status.ACTIVE, new Role());
        user4.getRole().setName("ROLE_CLIENT");

        passenger1 = new Passenger(1L,user3.getEmail(),user3.getName(),user3.getSurname(),DrivePassengerStatus.ACCEPTED,PaymentPassengerStatus.ACCEPTED,2000.0,true,2L);
        passenger2 = new Passenger(2L,user4.getEmail(),user4.getName(),user4.getSurname(),DrivePassengerStatus.ACCEPTED,PaymentPassengerStatus.ACCEPTED,2000.0,true,3L);

        passengers = new HashSet<>();
        passengers.add(passenger1);
        passengers.add(passenger2);

        address1 = new Address(1L, "Novi Sad", "Futoska", "11");
        car = new Car(1L, "Opel", "Corsa", "Siva", "NS123NS", CarBodyType.COUPE, Fuel.AUTOGAS, 5);
        client1 = new ClientsAccount(1L, user1, address1, null, "0658511701", null, BankStatus.EMPTY,false);
        client2 = new ClientsAccount(2L, user3, address1, null, "0658511701", null, BankStatus.EMPTY,false);
        client3 = new ClientsAccount(3L, user4, address1, null, "0658511701", null, BankStatus.EMPTY,false);

        driver = new DriversAccount(1L, user2, null, "0621926415", car, DriverStatus.AVAILABLE, true);
        driveJSON = "{\"bbox\": [19.803652, 45.248718, 19.865995, 45.26176], \"type\": \"FeatureCollection\", \"features\": [{\"bbox\": [19.803652, 45.248718, 19.865995, 45.26176], \"type\": \"Feature\", \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[19.835177, 45.250931], [19.83603, 45.251365], [19.836458, 45.251584], [19.836767, 45.251738], [19.836889, 45.251796], [19.836979, 45.251839], [19.837049, 45.251873], [19.837111, 45.251905], [19.837076, 45.251974], [19.837006, 45.25194], [19.83694, 45.251911], [19.8368, 45.251841], [19.836764, 45.251823], [19.836016, 45.251472], [19.834917, 45.250909], [19.833684, 45.250253], [19.833601, 45.250209], [19.833114, 45.249954], [19.83279, 45.249808], [19.832702, 45.249771], [19.832604, 45.249736], [19.832484, 45.249695], [19.832127, 45.249571], [19.831651, 45.249413], [19.831348, 45.249342], [19.831127, 45.2493], [19.830898, 45.249268], [19.830712, 45.249252], [19.829674, 45.249206], [19.82949, 45.249201], [19.829273, 45.249195], [19.828068, 45.249165], [19.827205, 45.249176], [19.826399, 45.249221], [19.825892, 45.249253], [19.825183, 45.2493], [19.825007, 45.249313], [19.824873, 45.249323], [19.824546, 45.249345], [19.824398, 45.24935], [19.824326, 45.249352], [19.822747, 45.249381], [19.8217, 45.249369], [19.821657, 45.249369], [19.820885, 45.249341], [19.820304, 45.249293], [19.819474, 45.24919], [19.818804, 45.249087], [19.817894, 45.248952], [19.817456, 45.248876], [19.817023, 45.248801], [19.816915, 45.248777], [19.816786, 45.24875], [19.816611, 45.248718], [19.816538, 45.248826], [19.813924, 45.25265], [19.813848, 45.25276], [19.813789, 45.252848], [19.813706, 45.252965], [19.811856, 45.255621], [19.811802, 45.255699], [19.811757, 45.255764], [19.811603, 45.25571], [19.811488, 45.255671], [19.810503, 45.255339], [19.809975, 45.255161], [19.809907, 45.255138], [19.806997, 45.254161], [19.806865, 45.254116], [19.806794, 45.254223], [19.806639, 45.254456], [19.806598, 45.254519], [19.806572, 45.254559], [19.806254, 45.255007], [19.806138, 45.255179], [19.805558, 45.256033], [19.804839, 45.257083], [19.804804, 45.257137], [19.804823, 45.257152], [19.804832, 45.257171], [19.804829, 45.25719], [19.80488, 45.257209], [19.805998, 45.257585], [19.8063, 45.257689], [19.806243, 45.257773], [19.805718, 45.258548], [19.805633, 45.258669], [19.805496, 45.258622], [19.805408, 45.258594], [19.803858, 45.258068], [19.803652, 45.257998], [19.803858, 45.258068], [19.803722, 45.258263], [19.803698, 45.258297], [19.803688, 45.258314], [19.803685, 45.258338], [19.803701, 45.258385], [19.803743, 45.258488], [19.804192, 45.258642], [19.805628, 45.259143], [19.806377, 45.259389], [19.807497, 45.259782], [19.808113, 45.259992], [19.808241, 45.260007], [19.808447, 45.260003], [19.808566, 45.259997], [19.808595, 45.259993], [19.808643, 45.25999], [19.808781, 45.26001], [19.808894, 45.26007], [19.808922, 45.260097], [19.808934, 45.260112], [19.809, 45.260204], [19.809137, 45.26032], [19.809369, 45.260427], [19.809551, 45.260494], [19.810321, 45.260764], [19.810942, 45.260982], [19.811053, 45.261022], [19.811146, 45.261056], [19.812912, 45.261713], [19.813018, 45.26176], [19.813105, 45.261687], [19.814434, 45.260539], [19.814515, 45.26047], [19.814559, 45.260427], [19.817448, 45.257828], [19.817546, 45.257758], [19.817615, 45.257708], [19.817754, 45.257755], [19.817785, 45.257766], [19.818187, 45.257904], [19.818839, 45.258123], [19.819344, 45.258294], [19.819481, 45.25834], [19.819561, 45.258366], [19.820519, 45.258672], [19.821185, 45.258801], [19.821293, 45.258818], [19.821377, 45.25883], [19.822317, 45.25894], [19.822707, 45.258961], [19.822841, 45.258966], [19.823017, 45.258978], [19.823169, 45.258991], [19.823193, 45.258993], [19.823594, 45.259036], [19.824144, 45.259101], [19.825501, 45.25924], [19.826498, 45.259353], [19.828141, 45.259544], [19.828255, 45.259556], [19.828419, 45.259578], [19.828669, 45.259629], [19.829471, 45.259842], [19.829646, 45.259894], [19.829791, 45.259934], [19.829969, 45.259984], [19.830014, 45.259997], [19.830931, 45.260245], [19.831076, 45.260284], [19.83185, 45.260497], [19.83197, 45.260531], [19.832106, 45.260568], [19.83229, 45.260619], [19.832426, 45.260657], [19.832468, 45.260668], [19.833034, 45.260825], [19.833818, 45.261038], [19.833991, 45.261088], [19.83406, 45.260956], [19.834442, 45.260266], [19.834703, 45.259874], [19.834864, 45.259691], [19.834905, 45.259653], [19.835043, 45.259526], [19.835188, 45.259417], [19.835201, 45.259408], [19.835315, 45.259337], [19.835462, 45.25927], [19.835701, 45.25919], [19.83584, 45.259147], [19.835989, 45.259125], [19.836046, 45.25911], [19.836106, 45.259099], [19.837276, 45.25891], [19.838506, 45.258668], [19.839677, 45.258469], [19.840936, 45.258232], [19.840927, 45.2582], [19.840661, 45.257268], [19.841457, 45.257134], [19.841512, 45.257125], [19.841658, 45.257098], [19.84162, 45.257077], [19.841578, 45.256998], [19.841547, 45.256628], [19.841432, 45.255829], [19.841432, 45.255712], [19.841478, 45.255588], [19.841682, 45.255255], [19.841917, 45.25494], [19.842248, 45.254457], [19.84227, 45.254432], [19.842284, 45.254413], [19.842381, 45.254281], [19.84245, 45.254195], [19.842534, 45.254098], [19.842555, 45.254074], [19.842595, 45.254028], [19.842688, 45.253941], [19.842905, 45.25376], [19.84331, 45.253548], [19.843418, 45.253504], [19.843579, 45.253455], [19.843869, 45.253394], [19.844209, 45.253362], [19.844728, 45.253387], [19.845568, 45.253465], [19.846166, 45.253527], [19.846377, 45.253548], [19.847272, 45.253637], [19.847313, 45.253641], [19.847618, 45.253668], [19.847712, 45.253676], [19.847818, 45.253686], [19.847908, 45.253694], [19.848317, 45.25373], [19.848479, 45.253748], [19.848918, 45.253788], [19.849353, 45.253828], [19.850252, 45.25391], [19.850401, 45.253924], [19.850553, 45.253938], [19.852419, 45.254134], [19.852582, 45.25415], [19.852766, 45.254169], [19.853883, 45.254284], [19.853981, 45.254294], [19.854156, 45.254328], [19.854913, 45.254402], [19.855046, 45.254415], [19.855105, 45.254421], [19.859571, 45.254846], [19.859649, 45.254854], [19.859894, 45.254875], [19.860124, 45.254894], [19.860338, 45.254908], [19.860406, 45.25491], [19.860501, 45.254914], [19.860638, 45.254912], [19.860837, 45.254909], [19.861363, 45.254864], [19.861658, 45.254839], [19.862736, 45.254715], [19.862793, 45.254708], [19.863169, 45.254667], [19.86357, 45.254624], [19.86371, 45.254592], [19.863921, 45.254529], [19.864023, 45.254486], [19.864266, 45.254369], [19.864434, 45.254289], [19.864616, 45.254196], [19.864868, 45.254062], [19.86523, 45.25382], [19.865533, 45.253597], [19.865995, 45.253276], [19.865973, 45.253218], [19.865936, 45.252956], [19.865936, 45.252841], [19.865925, 45.252801], [19.865675, 45.252567], [19.865641, 45.252508], [19.865621, 45.252364], [19.865714, 45.251512], [19.86568, 45.251295], [19.865624, 45.251153], [19.865496, 45.250898], [19.865415, 45.250791], [19.865305, 45.250637], [19.865222, 45.250586], [19.865132, 45.250586], [19.864977, 45.250671], [19.864575, 45.250974], [19.864403, 45.25113], [19.864351, 45.251126], [19.863919, 45.250988], [19.863825, 45.25097], [19.863713, 45.250971], [19.8636, 45.25101], [19.863273, 45.251126], [19.863171, 45.251173], [19.863118, 45.251256], [19.863125, 45.251494], [19.863109, 45.251531], [19.863085, 45.251557]]}, \"properties\": {\"summary\": {\"distance\": 10523.2, \"duration\": 1413.7}, \"segments\": [{\"steps\": [{\"name\": \"Футошка\", \"type\": 11, \"distance\": 186.2, \"duration\": 29.7, \"way_points\": [0, 7], \"instruction\": \"Head northeast on Футошка\"}, {\"name\": \"Јеврејска\", \"type\": 6, \"distance\": 1709.1, \"duration\": 187.4, \"way_points\": [7, 53], \"instruction\": \"Continue straight onto Јеврејска\"}, {\"name\": \"Булевар Европе, 111\", \"type\": 1, \"distance\": 870.7, \"duration\": 62.7, \"way_points\": [53, 61], \"instruction\": \"Turn right onto Булевар Европе, 111\"}, {\"name\": \"Хаџи Рувимова\", \"type\": 0, \"distance\": 424.5, \"duration\": 34, \"way_points\": [61, 68], \"instruction\": \"Turn left onto Хаџи Рувимова\"}, {\"name\": \"Стојана Новаковића\", \"type\": 1, \"distance\": 372.7, \"duration\": 89.4, \"way_points\": [68, 77], \"instruction\": \"Turn right onto Стојана Новаковића\"}, {\"name\": \"Ђорђа Никшића Јохана\", \"type\": 7, \"distance\": 134.4, \"duration\": 32.3, \"way_points\": [77, 83], \"exit_number\": 1, \"instruction\": \"Enter the roundabout and take the 1st exit onto Ђорђа Никшића Јохана\"}, {\"name\": \"Милана Јешића Ибре\", \"type\": 0, \"distance\": 120.8, \"duration\": 29, \"way_points\": [83, 86], \"instruction\": \"Turn left onto Милана Јешића Ибре\"}, {\"name\": \"Родољуба Чолаковића\", \"type\": 0, \"distance\": 172.1, \"duration\": 41.3, \"way_points\": [86, 90], \"instruction\": \"Turn left onto Родољуба Чолаковића\"}, {\"name\": \"-\", \"type\": 10, \"distance\": 0, \"duration\": 0, \"way_points\": [90, 90], \"instruction\": \"Arrive at Родољуба Чолаковића, on the left\"}], \"distance\": 3990.7, \"duration\": 505.7}, {\"steps\": [{\"name\": \"Родољуба Чолаковића\", \"type\": 11, \"distance\": 17.9, \"duration\": 4.3, \"way_points\": [90, 91], \"instruction\": \"Head northeast on Родољуба Чолаковића\"}, {\"name\": \"-\", \"type\": 0, \"distance\": 50.4, \"duration\": 12.1, \"way_points\": [91, 97], \"instruction\": \"Turn left\"}, {\"name\": \"Булевар војводе Степе, 12\", \"type\": 1, \"distance\": 164.5, \"duration\": 19.1, \"way_points\": [97, 99], \"instruction\": \"Turn right onto Булевар војводе Степе, 12\"}, {\"name\": \"Булевар војводе Степе, 12\", \"type\": 6, \"distance\": 251.8, \"duration\": 18.1, \"way_points\": [99, 105], \"instruction\": \"Continue straight onto Булевар војводе Степе, 12\"}, {\"name\": \"Корнелија Станковића, 12\", \"type\": 7, \"distance\": 405.8, \"duration\": 35.9, \"way_points\": [105, 121], \"exit_number\": 2, \"instruction\": \"Enter the roundabout and take the 2nd exit onto Корнелија Станковића, 12\"}, {\"name\": \"Илије Бирчанина\", \"type\": 1, \"distance\": 576.8, \"duration\": 138.4, \"way_points\": [121, 128], \"instruction\": \"Turn right onto Илије Бирчанина\"}, {\"name\": \"Хаџи Рувимова\", \"type\": 0, \"distance\": 1345.9, \"duration\": 131.5, \"way_points\": [128, 169], \"instruction\": \"Turn left onto Хаџи Рувимова\"}, {\"name\": \"Браће Јовандић\", \"type\": 1, \"distance\": 281.4, \"duration\": 33.8, \"way_points\": [169, 182], \"instruction\": \"Turn right onto Браће Јовандић\"}, {\"name\": \"Војводе Бојовића\", \"type\": 6, \"distance\": 399.8, \"duration\": 36, \"way_points\": [182, 188], \"instruction\": \"Continue straight onto Војводе Бојовића\"}, {\"name\": \"Вука Караџића\", \"type\": 1, \"distance\": 109.4, \"duration\": 26.3, \"way_points\": [188, 190], \"instruction\": \"Turn right onto Вука Караџића\"}, {\"name\": \"Масарикова\", \"type\": 0, \"distance\": 80.4, \"duration\": 19.3, \"way_points\": [190, 193], \"instruction\": \"Turn left onto Масарикова\"}, {\"name\": \"Шафарикова\", \"type\": 3, \"distance\": 156.8, \"duration\": 14.1, \"way_points\": [193, 198], \"instruction\": \"Turn sharp right onto Шафарикова\"}, {\"name\": \"Успенска\", \"type\": 12, \"distance\": 153.7, \"duration\": 22, \"way_points\": [198, 202], \"instruction\": \"Keep left onto Успенска\"}, {\"name\": \"Успенска\", \"type\": 12, \"distance\": 452.2, \"duration\": 59.9, \"way_points\": [202, 222], \"instruction\": \"Keep left onto Успенска\"}, {\"name\": \"Булевар Михајла Пупина\", \"type\": 6, \"distance\": 527, \"duration\": 70.3, \"way_points\": [222, 238], \"instruction\": \"Continue straight onto Булевар Михајла Пупина\"}, {\"name\": \"Булевар Михајла Пупина\", \"type\": 12, \"distance\": 467.5, \"duration\": 53.6, \"way_points\": [238, 245], \"instruction\": \"Keep left onto Булевар Михајла Пупина\"}, {\"name\": \"-\", \"type\": 12, \"distance\": 534.6, \"duration\": 79.8, \"way_points\": [245, 267], \"instruction\": \"Keep left\"}, {\"name\": \"-\", \"type\": 1, \"distance\": 48.7, \"duration\": 11.7, \"way_points\": [267, 270], \"instruction\": \"Turn right\"}, {\"name\": \"-\", \"type\": 13, \"distance\": 267.3, \"duration\": 64.1, \"way_points\": [270, 281], \"instruction\": \"Keep right\"}, {\"name\": \"-\", \"type\": 13, \"distance\": 90.5, \"duration\": 21.7, \"way_points\": [281, 285], \"instruction\": \"Keep right\"}, {\"name\": \"-\", \"type\": 0, \"distance\": 150.1, \"duration\": 36, \"way_points\": [285, 296], \"instruction\": \"Turn left\"}, {\"name\": \"-\", \"type\": 10, \"distance\": 0, \"duration\": 0, \"way_points\": [296, 296], \"instruction\": \"Arrive at your destination, on the right\"}], \"distance\": 6532.5, \"duration\": 908}], \"way_points\": [0, 90, 296]}}], \"metadata\": {\"query\": {\"format\": \"geojson\", \"profile\": \"driving-car\", \"preference\": \"shortest\", \"coordinates\": [[19.83527, 45.25084], [19.80363, 45.25803], [19.8636, 45.25179]]}, \"engine\": {\"version\": \"6.8.0\", \"build_date\": \"2022-10-21T14:34:31Z\", \"graph_date\": \"2023-01-29T17:33:07Z\"}, \"service\": \"routing\", \"timestamp\": 1675318990675, \"attribution\": \"openrouteservice.org | OpenStreetMap contributors\"}}";
        drive = new Drive(1L, new ArrayList<RealAddress>(), 2.3, 23.6, 1245.40, passengers, 5, 0, 0, 0, client1, driveJSON, driver, DriveStatus.DRIVER_WAITING, false, new Date(), DriveType.NOW, 0.0, null, null,1L);
        drives.add(drive);
        bankTransaction = new BankTransaction();
        bankTransaction.setId(1L);

    }

 


    @Test
    @DisplayName("Should throw EmailNotFoundException when changing InDrive status for passengers")
    public void shouldThrowEmailNotFoundExceptionStartDrive() throws EmailNotFoundException {
        Mockito.doThrow(new EmailNotFoundException("Email ne postoji")).when(userService).findClientsAccount(any());
        String message = assertThrows(
                EmailNotFoundException.class,
                () ->
                {
                    driveService.passengerInDriveStatus(passengers);
                }
        ).getMessage();
        assertEquals("Email ne postoji",message);
    }

    @Test
    @DisplayName("Should throw EmailNotFoundException when changing InDrive status for passengers")
    public void shouldThrowEmailNotFoundExceptionEndDrive() throws EmailNotFoundException {
        Mockito.doThrow(new EmailNotFoundException("Email ne postoji")).when(userService).findClientsAccount(any());
        String message = assertThrows(
                EmailNotFoundException.class,
                () ->
                {
                    driveService.passengerNotInDriveStatus(passengers);
                }
        ).getMessage();
        assertEquals("Email ne postoji",message);
    }


    @Test
    @DisplayName("Should change inDrive to be true for passengers")
    public void shouldChangeInDriveToTrue() throws EmailNotFoundException {
        Mockito.doReturn(client2).when(userService).findClientsAccount("mika@gmail.com");
        Mockito.doReturn(client3).when(userService).findClientsAccount("pero@gmail.com");
        Mockito.doReturn(null).when(userService).saveCurrent(any());

        driveService.passengerInDriveStatus(passengers);
        assertEquals(true,client2.isInDrive());
        assertEquals(true,client3.isInDrive());

    }

    @Test
    @DisplayName("Should change inDrive to be false for passengers")
    public void shouldChangeInDriveToFalse() throws EmailNotFoundException {
        client2.setInDrive(true);
        client3.setInDrive(true);
        Mockito.doReturn(client2).when(userService).findClientsAccount("mika@gmail.com");
        Mockito.doReturn(client3).when(userService).findClientsAccount("pero@gmail.com");
        Mockito.doReturn(null).when(userService).saveCurrent(any());
        assertEquals(true, client2.isInDrive());
        assertEquals(true, client3.isInDrive());
        driveService.passengerNotInDriveStatus(passengers);
        assertEquals(false, client2.isInDrive());
        assertEquals(false, client3.isInDrive());

    }

    @Test
    @DisplayName("Should not start drive")
    public void shouldNotStartDrive() throws EmailNotFoundException {

        Mockito.doReturn(null).when(driveService).getCurrentDrive();


        assertEquals("Nema voznje.",driveService.startDrive());

        //verify(driveRepository, times(1)).findByDriver(driver);
        verify(driveService,times(0)).finalizePassengersTransactions(drive.getPassengers());
        verify(rideSimulationService,times(0)).createRideSim(drive);


    }
    @Test
    @DisplayName("Should not end drive")
    public void shouldNotEndDrive() throws EmailNotFoundException {

        Mockito.doReturn(null).when(driveService).getCurrentDrive();
        assertEquals("Nema trenutne voznje.",driveService.endDrive());

    }

    @Test
    @DisplayName("Should start drive")
    public void shouldStartRide() throws EmailNotFoundException {

        Mockito.doReturn(drive).when(driveService).getCurrentDrive();
        Mockito.doReturn(client1).when(userService).saveCurrent(client1);
        Mockito.doNothing().when(driveService).passengerInDriveStatus(any());
        Mockito.doReturn(drive).when(driveRepository).save(drive);
        Mockito.doReturn(null).when(bankService).transactionFinalized(any());

        Mockito.doNothing().when(userService).updateDriverStatus(any());
        Mockito.doReturn(null).when(rideSimulationService).createRideSim(any());

        assertEquals("Zapoceta nova voznja.",driveService.startDrive());

        //verify(driveRepository, times(1)).findByDriver(driver);
        verify(driveService,times(0)).finalizePassengersTransactions(drive.getPassengers());
        verify(rideSimulationService,times(1)).createRideSim(drive);

    }

    @Test
    @DisplayName("Should end drive")
    public void shouldEndRide() throws EmailNotFoundException {

        drive.setDriveType(DriveType.NOW);
        drive.setDriveStatus(DriveStatus.DRIVE_STARTED);
        driver.setDriverStatus(DriverStatus.BUSY);

        Mockito.doReturn(drive).when(driveService).getCurrentDrive();
        Mockito.doReturn(client1).when(userService).saveCurrent(client1);
        Mockito.doNothing().when(driveService).passengerNotInDriveStatus(any());
        Mockito.doReturn(drive).when(driveRepository).save(drive);

        Mockito.doNothing().when(userService).updateDriverStatus(any());

        assertEquals(DriveStatus.DRIVE_STARTED,drive.getDriveStatus());
        assertEquals("Voznja zavrsena",driveService.endDrive());
        assertEquals(DriveStatus.DRIVE_ENDED,drive.getDriveStatus());
         //verify(driveRepository, times(1)).findByDriver(driver);


    }
}
