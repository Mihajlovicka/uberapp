package com.example.demo.service;


import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.TransactionIdDoesNotExistException;
import com.example.demo.fakeBank.*;
import com.example.demo.model.*;
import com.example.demo.repository.ClientsRepository;
import com.example.demo.repository.DriveRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class DriveReservationServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private DriveRepository driveRepository;
    @Mock
    private ClientsRepository clientsRepository;
    @Mock
    private BankService bankService;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private DriveService driveService;

    private ClientsAccount clientWithMoney;

    private ClientsAccount clientWithoutMoney;

    private DriversAccount driverAvailable;


    private Drive driveNoDriver_NoPassengers;

    private Drive driveNoDriver_WithPassengers;

    private Passenger passWithMoney;

    private Drive failedDrive;

    private BankTransaction bankTransaction;

    private Drive driveOwnerNoMoneyStatusOwnerWaiting;

    private Drive driveOwnerNoMoneyStatusFailed;

    private Drive driveOwnerMoneyStatusOwnerWaiting_NoDriver;

    private Drive driveOwnerMoneyStatusDriverWaiting_NoDriver;

    private Drive driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered;

    private Drive driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered;

    private Drive driveOwnerMoneyStatusDriverWaiting_NoDriver_future;

    private Drive driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive;

    private Drive driveOwnerMoneyStatusDriverWaiting_NoDriver_futureDrive;

    private Drive driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited;

    private Drive driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited;

    private Drive driveWithAvailableDriver;

    private Drive driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture;

    private Drive driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture;

    private Drive driveOwnerMoney_DriveStatusDriveFailed_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture;

    private Drive driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow;


    private Drive driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow;

    private Passenger passengerCantAfford;

    private Passenger passengerRejectedPayment;

    //payment accepted - owner accepted - has passengers - participation answered - bill splited - pass cannot afford
    private Drive driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAfford;

    private Drive driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected;

    private Drive driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected_NewPrice;

    private Drive driveOwnerMoney_DriveStatus_OwnerPaymentWaiting_WithPassengers_ParticipationAnswered_PassengersCanAfford;

    private Drive driveOwnerMoney_DriveStatus_PaymentWaiting_WithPassengers_ParticipationAnswered_PassengersCanAfford;
    private Passenger passCanAfford;

    private BankTransaction passengersBankTransaction;

    private ClientsAccount clientAccPassCanAfford;

    private Drive drive_passengerPayment_OneAccepted_OtherWaiting;

    private Drive drive_passengerPayment_OneAccepted_OtherWaiting2;

    private Drive passengerAcceptedPaymentWaiting;

    private Drive passengerAcceptedPaymentPaymentDone;

    private Drive passengerAcceptedPaymentDriverWaiting;

    private Drive passengerAcceptedPaymentDriveFailed;

    private Drive passengerAcceptedPaymentDriverAvailable;

    private BankTransaction rejectedBankTransaction;


    @BeforeEach
    public void setters(){
        //client with money
        User user1 = new User(1L, "jelena@gmail.com", "Jelena", "Manojlovic", "jelena@gmail.com", "$2a$10$rXb3n7ggvOSi6OPcExZWTO.0Zp2LEW.RCOfoKTLkcMdxR79UO9HGy", Status.ACTIVE, new Role());
        user1.getRole().setName("ROLE_CLIENT");

        //client withouth money
        User user2 = new User(2L, "milan@gmail.com", "Milan", "Milakovic", "milan@gmail.com", "$2a$10$rXb3n7ggvOSi6OPcExZWTO.0Zp2LEW.RCOfoKTLkcMdxR79UO9HGy", Status.ACTIVE, new Role());
        user2.getRole().setName("ROLE_CLIENT");

        //driver available
        User user3 = new User(3L, "dragan@gmail.com", "Dragan", "Manojlovic", "dragan@gmail.com", "$2a$10$rXb3n7ggvOSi6OPcExZWTO.0Zp2LEW.RCOfoKTLkcMdxR79UO9HGy", Status.ACTIVE, new Role());
        user3.getRole().setName("ROLE_DRIVER");

        //driver not available
        User user4 = new User(4L, "stevica@gmail.com", "Stevica", "Stefanovic", "stevica@gmail.com", "$2a$10$rXb3n7ggvOSi6OPcExZWTO.0Zp2LEW.RCOfoKTLkcMdxR79UO9HGy", Status.ACTIVE, new Role());
        user4.getRole().setName("ROLE_DRIVER");

        //client with money
        User user5 = new User(5L, "passenger@gmail.com", "Jovica", "Jovic", "passenger@gmail.com", "$2a$10$rXb3n7ggvOSi6OPcExZWTO.0Zp2LEW.RCOfoKTLkcMdxR79UO9HGy", Status.ACTIVE, new Role());
        user5.getRole().setName("ROLE_CLIENT");

        Address address = new Address(1L, "Novi Sad", "Cankareva", "22a");

        ClientsBankAccount clientsBankAccountWithMoney = new ClientsBankAccount(1L, 100000.0, "123456789012345678", "jelena@gmail.com", "Jelena", "Manojlovic");
        ClientsBankAccount clientsBankAccountWithoutMoney = new ClientsBankAccount(2L, 10.0, "098765432198765432", "milan@gmail.com", "Milan", "Milakovic");

        clientWithMoney = new ClientsAccount(1L, user1, address, null, "123456789", clientsBankAccountWithMoney, BankStatus.ACTIVE, false);
        clientWithoutMoney = new ClientsAccount(2L, user2, address, null, "123456789", clientsBankAccountWithoutMoney, BankStatus.ACTIVE, false);

        clientAccPassCanAfford = new ClientsAccount(3L, user5, address, null, "123456789", clientsBankAccountWithMoney, BankStatus.ACTIVE, false);


        Car carAvailable = new Car(1L, "AUDI", "TT", "BLACK", "SO034FN", CarBodyType.COUPE, Fuel.GASOLINE, 4);
        carAvailable.setCarOnMap(true);
        carAvailable.setCurrentLocation(new Location(20.0, 20.0));


        driverAvailable = new DriversAccount(1L, user3, null, "123456789", carAvailable, DriverStatus.AVAILABLE, true);

        RealAddress stop1 = new RealAddress();
        stop1.setId(1);
        stop1.setAddress("Cankreva 22 Novi Sad 21000");
        stop1.setName("Cankreva 22 Novi Sad 21000");
        stop1.setLocation(new Location(45.2322, 65.99));


        String driveJSON = "{\"bbox\": [19.803652, 45.248718, 19.865995, 45.26176], \"type\": \"FeatureCollection\", \"features\": [{\"bbox\": [19.803652, 45.248718, 19.865995, 45.26176], \"type\": \"Feature\", \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[19.835177, 45.250931], [19.83603, 45.251365], [19.836458, 45.251584], [19.836767, 45.251738], [19.836889, 45.251796], [19.836979, 45.251839], [19.837049, 45.251873], [19.837111, 45.251905], [19.837076, 45.251974], [19.837006, 45.25194], [19.83694, 45.251911], [19.8368, 45.251841], [19.836764, 45.251823], [19.836016, 45.251472], [19.834917, 45.250909], [19.833684, 45.250253], [19.833601, 45.250209], [19.833114, 45.249954], [19.83279, 45.249808], [19.832702, 45.249771], [19.832604, 45.249736], [19.832484, 45.249695], [19.832127, 45.249571], [19.831651, 45.249413], [19.831348, 45.249342], [19.831127, 45.2493], [19.830898, 45.249268], [19.830712, 45.249252], [19.829674, 45.249206], [19.82949, 45.249201], [19.829273, 45.249195], [19.828068, 45.249165], [19.827205, 45.249176], [19.826399, 45.249221], [19.825892, 45.249253], [19.825183, 45.2493], [19.825007, 45.249313], [19.824873, 45.249323], [19.824546, 45.249345], [19.824398, 45.24935], [19.824326, 45.249352], [19.822747, 45.249381], [19.8217, 45.249369], [19.821657, 45.249369], [19.820885, 45.249341], [19.820304, 45.249293], [19.819474, 45.24919], [19.818804, 45.249087], [19.817894, 45.248952], [19.817456, 45.248876], [19.817023, 45.248801], [19.816915, 45.248777], [19.816786, 45.24875], [19.816611, 45.248718], [19.816538, 45.248826], [19.813924, 45.25265], [19.813848, 45.25276], [19.813789, 45.252848], [19.813706, 45.252965], [19.811856, 45.255621], [19.811802, 45.255699], [19.811757, 45.255764], [19.811603, 45.25571], [19.811488, 45.255671], [19.810503, 45.255339], [19.809975, 45.255161], [19.809907, 45.255138], [19.806997, 45.254161], [19.806865, 45.254116], [19.806794, 45.254223], [19.806639, 45.254456], [19.806598, 45.254519], [19.806572, 45.254559], [19.806254, 45.255007], [19.806138, 45.255179], [19.805558, 45.256033], [19.804839, 45.257083], [19.804804, 45.257137], [19.804823, 45.257152], [19.804832, 45.257171], [19.804829, 45.25719], [19.80488, 45.257209], [19.805998, 45.257585], [19.8063, 45.257689], [19.806243, 45.257773], [19.805718, 45.258548], [19.805633, 45.258669], [19.805496, 45.258622], [19.805408, 45.258594], [19.803858, 45.258068], [19.803652, 45.257998], [19.803858, 45.258068], [19.803722, 45.258263], [19.803698, 45.258297], [19.803688, 45.258314], [19.803685, 45.258338], [19.803701, 45.258385], [19.803743, 45.258488], [19.804192, 45.258642], [19.805628, 45.259143], [19.806377, 45.259389], [19.807497, 45.259782], [19.808113, 45.259992], [19.808241, 45.260007], [19.808447, 45.260003], [19.808566, 45.259997], [19.808595, 45.259993], [19.808643, 45.25999], [19.808781, 45.26001], [19.808894, 45.26007], [19.808922, 45.260097], [19.808934, 45.260112], [19.809, 45.260204], [19.809137, 45.26032], [19.809369, 45.260427], [19.809551, 45.260494], [19.810321, 45.260764], [19.810942, 45.260982], [19.811053, 45.261022], [19.811146, 45.261056], [19.812912, 45.261713], [19.813018, 45.26176], [19.813105, 45.261687], [19.814434, 45.260539], [19.814515, 45.26047], [19.814559, 45.260427], [19.817448, 45.257828], [19.817546, 45.257758], [19.817615, 45.257708], [19.817754, 45.257755], [19.817785, 45.257766], [19.818187, 45.257904], [19.818839, 45.258123], [19.819344, 45.258294], [19.819481, 45.25834], [19.819561, 45.258366], [19.820519, 45.258672], [19.821185, 45.258801], [19.821293, 45.258818], [19.821377, 45.25883], [19.822317, 45.25894], [19.822707, 45.258961], [19.822841, 45.258966], [19.823017, 45.258978], [19.823169, 45.258991], [19.823193, 45.258993], [19.823594, 45.259036], [19.824144, 45.259101], [19.825501, 45.25924], [19.826498, 45.259353], [19.828141, 45.259544], [19.828255, 45.259556], [19.828419, 45.259578], [19.828669, 45.259629], [19.829471, 45.259842], [19.829646, 45.259894], [19.829791, 45.259934], [19.829969, 45.259984], [19.830014, 45.259997], [19.830931, 45.260245], [19.831076, 45.260284], [19.83185, 45.260497], [19.83197, 45.260531], [19.832106, 45.260568], [19.83229, 45.260619], [19.832426, 45.260657], [19.832468, 45.260668], [19.833034, 45.260825], [19.833818, 45.261038], [19.833991, 45.261088], [19.83406, 45.260956], [19.834442, 45.260266], [19.834703, 45.259874], [19.834864, 45.259691], [19.834905, 45.259653], [19.835043, 45.259526], [19.835188, 45.259417], [19.835201, 45.259408], [19.835315, 45.259337], [19.835462, 45.25927], [19.835701, 45.25919], [19.83584, 45.259147], [19.835989, 45.259125], [19.836046, 45.25911], [19.836106, 45.259099], [19.837276, 45.25891], [19.838506, 45.258668], [19.839677, 45.258469], [19.840936, 45.258232], [19.840927, 45.2582], [19.840661, 45.257268], [19.841457, 45.257134], [19.841512, 45.257125], [19.841658, 45.257098], [19.84162, 45.257077], [19.841578, 45.256998], [19.841547, 45.256628], [19.841432, 45.255829], [19.841432, 45.255712], [19.841478, 45.255588], [19.841682, 45.255255], [19.841917, 45.25494], [19.842248, 45.254457], [19.84227, 45.254432], [19.842284, 45.254413], [19.842381, 45.254281], [19.84245, 45.254195], [19.842534, 45.254098], [19.842555, 45.254074], [19.842595, 45.254028], [19.842688, 45.253941], [19.842905, 45.25376], [19.84331, 45.253548], [19.843418, 45.253504], [19.843579, 45.253455], [19.843869, 45.253394], [19.844209, 45.253362], [19.844728, 45.253387], [19.845568, 45.253465], [19.846166, 45.253527], [19.846377, 45.253548], [19.847272, 45.253637], [19.847313, 45.253641], [19.847618, 45.253668], [19.847712, 45.253676], [19.847818, 45.253686], [19.847908, 45.253694], [19.848317, 45.25373], [19.848479, 45.253748], [19.848918, 45.253788], [19.849353, 45.253828], [19.850252, 45.25391], [19.850401, 45.253924], [19.850553, 45.253938], [19.852419, 45.254134], [19.852582, 45.25415], [19.852766, 45.254169], [19.853883, 45.254284], [19.853981, 45.254294], [19.854156, 45.254328], [19.854913, 45.254402], [19.855046, 45.254415], [19.855105, 45.254421], [19.859571, 45.254846], [19.859649, 45.254854], [19.859894, 45.254875], [19.860124, 45.254894], [19.860338, 45.254908], [19.860406, 45.25491], [19.860501, 45.254914], [19.860638, 45.254912], [19.860837, 45.254909], [19.861363, 45.254864], [19.861658, 45.254839], [19.862736, 45.254715], [19.862793, 45.254708], [19.863169, 45.254667], [19.86357, 45.254624], [19.86371, 45.254592], [19.863921, 45.254529], [19.864023, 45.254486], [19.864266, 45.254369], [19.864434, 45.254289], [19.864616, 45.254196], [19.864868, 45.254062], [19.86523, 45.25382], [19.865533, 45.253597], [19.865995, 45.253276], [19.865973, 45.253218], [19.865936, 45.252956], [19.865936, 45.252841], [19.865925, 45.252801], [19.865675, 45.252567], [19.865641, 45.252508], [19.865621, 45.252364], [19.865714, 45.251512], [19.86568, 45.251295], [19.865624, 45.251153], [19.865496, 45.250898], [19.865415, 45.250791], [19.865305, 45.250637], [19.865222, 45.250586], [19.865132, 45.250586], [19.864977, 45.250671], [19.864575, 45.250974], [19.864403, 45.25113], [19.864351, 45.251126], [19.863919, 45.250988], [19.863825, 45.25097], [19.863713, 45.250971], [19.8636, 45.25101], [19.863273, 45.251126], [19.863171, 45.251173], [19.863118, 45.251256], [19.863125, 45.251494], [19.863109, 45.251531], [19.863085, 45.251557]]}, \"properties\": {\"summary\": {\"distance\": 10523.2, \"duration\": 1413.7}, \"segments\": [{\"steps\": [{\"name\": \"Футошка\", \"type\": 11, \"distance\": 186.2, \"duration\": 29.7, \"way_points\": [0, 7], \"instruction\": \"Head northeast on Футошка\"}, {\"name\": \"Јеврејска\", \"type\": 6, \"distance\": 1709.1, \"duration\": 187.4, \"way_points\": [7, 53], \"instruction\": \"Continue straight onto Јеврејска\"}, {\"name\": \"Булевар Европе, 111\", \"type\": 1, \"distance\": 870.7, \"duration\": 62.7, \"way_points\": [53, 61], \"instruction\": \"Turn right onto Булевар Европе, 111\"}, {\"name\": \"Хаџи Рувимова\", \"type\": 0, \"distance\": 424.5, \"duration\": 34, \"way_points\": [61, 68], \"instruction\": \"Turn left onto Хаџи Рувимова\"}, {\"name\": \"Стојана Новаковића\", \"type\": 1, \"distance\": 372.7, \"duration\": 89.4, \"way_points\": [68, 77], \"instruction\": \"Turn right onto Стојана Новаковића\"}, {\"name\": \"Ђорђа Никшића Јохана\", \"type\": 7, \"distance\": 134.4, \"duration\": 32.3, \"way_points\": [77, 83], \"exit_number\": 1, \"instruction\": \"Enter the roundabout and take the 1st exit onto Ђорђа Никшића Јохана\"}, {\"name\": \"Милана Јешића Ибре\", \"type\": 0, \"distance\": 120.8, \"duration\": 29, \"way_points\": [83, 86], \"instruction\": \"Turn left onto Милана Јешића Ибре\"}, {\"name\": \"Родољуба Чолаковића\", \"type\": 0, \"distance\": 172.1, \"duration\": 41.3, \"way_points\": [86, 90], \"instruction\": \"Turn left onto Родољуба Чолаковића\"}, {\"name\": \"-\", \"type\": 10, \"distance\": 0, \"duration\": 0, \"way_points\": [90, 90], \"instruction\": \"Arrive at Родољуба Чолаковића, on the left\"}], \"distance\": 3990.7, \"duration\": 505.7}, {\"steps\": [{\"name\": \"Родољуба Чолаковића\", \"type\": 11, \"distance\": 17.9, \"duration\": 4.3, \"way_points\": [90, 91], \"instruction\": \"Head northeast on Родољуба Чолаковића\"}, {\"name\": \"-\", \"type\": 0, \"distance\": 50.4, \"duration\": 12.1, \"way_points\": [91, 97], \"instruction\": \"Turn left\"}, {\"name\": \"Булевар војводе Степе, 12\", \"type\": 1, \"distance\": 164.5, \"duration\": 19.1, \"way_points\": [97, 99], \"instruction\": \"Turn right onto Булевар војводе Степе, 12\"}, {\"name\": \"Булевар војводе Степе, 12\", \"type\": 6, \"distance\": 251.8, \"duration\": 18.1, \"way_points\": [99, 105], \"instruction\": \"Continue straight onto Булевар војводе Степе, 12\"}, {\"name\": \"Корнелија Станковића, 12\", \"type\": 7, \"distance\": 405.8, \"duration\": 35.9, \"way_points\": [105, 121], \"exit_number\": 2, \"instruction\": \"Enter the roundabout and take the 2nd exit onto Корнелија Станковића, 12\"}, {\"name\": \"Илије Бирчанина\", \"type\": 1, \"distance\": 576.8, \"duration\": 138.4, \"way_points\": [121, 128], \"instruction\": \"Turn right onto Илије Бирчанина\"}, {\"name\": \"Хаџи Рувимова\", \"type\": 0, \"distance\": 1345.9, \"duration\": 131.5, \"way_points\": [128, 169], \"instruction\": \"Turn left onto Хаџи Рувимова\"}, {\"name\": \"Браће Јовандић\", \"type\": 1, \"distance\": 281.4, \"duration\": 33.8, \"way_points\": [169, 182], \"instruction\": \"Turn right onto Браће Јовандић\"}, {\"name\": \"Војводе Бојовића\", \"type\": 6, \"distance\": 399.8, \"duration\": 36, \"way_points\": [182, 188], \"instruction\": \"Continue straight onto Војводе Бојовића\"}, {\"name\": \"Вука Караџића\", \"type\": 1, \"distance\": 109.4, \"duration\": 26.3, \"way_points\": [188, 190], \"instruction\": \"Turn right onto Вука Караџића\"}, {\"name\": \"Масарикова\", \"type\": 0, \"distance\": 80.4, \"duration\": 19.3, \"way_points\": [190, 193], \"instruction\": \"Turn left onto Масарикова\"}, {\"name\": \"Шафарикова\", \"type\": 3, \"distance\": 156.8, \"duration\": 14.1, \"way_points\": [193, 198], \"instruction\": \"Turn sharp right onto Шафарикова\"}, {\"name\": \"Успенска\", \"type\": 12, \"distance\": 153.7, \"duration\": 22, \"way_points\": [198, 202], \"instruction\": \"Keep left onto Успенска\"}, {\"name\": \"Успенска\", \"type\": 12, \"distance\": 452.2, \"duration\": 59.9, \"way_points\": [202, 222], \"instruction\": \"Keep left onto Успенска\"}, {\"name\": \"Булевар Михајла Пупина\", \"type\": 6, \"distance\": 527, \"duration\": 70.3, \"way_points\": [222, 238], \"instruction\": \"Continue straight onto Булевар Михајла Пупина\"}, {\"name\": \"Булевар Михајла Пупина\", \"type\": 12, \"distance\": 467.5, \"duration\": 53.6, \"way_points\": [238, 245], \"instruction\": \"Keep left onto Булевар Михајла Пупина\"}, {\"name\": \"-\", \"type\": 12, \"distance\": 534.6, \"duration\": 79.8, \"way_points\": [245, 267], \"instruction\": \"Keep left\"}, {\"name\": \"-\", \"type\": 1, \"distance\": 48.7, \"duration\": 11.7, \"way_points\": [267, 270], \"instruction\": \"Turn right\"}, {\"name\": \"-\", \"type\": 13, \"distance\": 267.3, \"duration\": 64.1, \"way_points\": [270, 281], \"instruction\": \"Keep right\"}, {\"name\": \"-\", \"type\": 13, \"distance\": 90.5, \"duration\": 21.7, \"way_points\": [281, 285], \"instruction\": \"Keep right\"}, {\"name\": \"-\", \"type\": 0, \"distance\": 150.1, \"duration\": 36, \"way_points\": [285, 296], \"instruction\": \"Turn left\"}, {\"name\": \"-\", \"type\": 10, \"distance\": 0, \"duration\": 0, \"way_points\": [296, 296], \"instruction\": \"Arrive at your destination, on the right\"}], \"distance\": 6532.5, \"duration\": 908}], \"way_points\": [0, 90, 296]}}], \"metadata\": {\"query\": {\"format\": \"geojson\", \"profile\": \"driving-car\", \"preference\": \"shortest\", \"coordinates\": [[19.83527, 45.25084], [19.80363, 45.25803], [19.8636, 45.25179]]}, \"engine\": {\"version\": \"6.8.0\", \"build_date\": \"2022-10-21T14:34:31Z\", \"graph_date\": \"2023-01-29T17:33:07Z\"}, \"service\": \"routing\", \"timestamp\": 1675318990675, \"attribution\": \"openrouteservice.org | OpenStreetMap contributors\"}}";


        //owner ima para, nema passengere, nema vozaca, owner payment waiting, tip voznje je future
        driveNoDriver_NoPassengers = new Drive(1L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, null, false, new Date(), DriveType.FUTURE, 0.0, new Date(), new Date(), -1L);
        driveNoDriver_NoPassengers.getStops().add(stop1);
        driveNoDriver_NoPassengers.setOwnerDebit(driveNoDriver_NoPassengers.getPrice());

        //owner ima para, nema passengere, ima vozaca, driver   waiting, tip voznje je now (bilo future pa ispravljeno...)
        Drive driveWithDriver_NoPassengers = new Drive(2L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, driverAvailable, DriveStatus.DRIVER_WAITING, false, new Date(), DriveType.NOW, 0.0, new Date(), new Date(), -1L);
        driveWithDriver_NoPassengers.getStops().add(stop1);
        driveNoDriver_NoPassengers.setOwnerDebit(driveNoDriver_NoPassengers.getPrice());

        failedDrive = new Drive(3L, new ArrayList<RealAddress>(), 20.0, 30.0, 1000.0, new HashSet<Passenger>(), 5, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.DRIVE_FAILED, false, new Date(), DriveType.NOW, 0.0, new Date(), new Date(), -1L);
        failedDrive.getStops().add(stop1);
        failedDrive.setOwnerDebit(failedDrive.getPrice());



        passWithMoney = new Passenger(1L, user5.getEmail(), user5.getName(), user5.getSurname(), DrivePassengerStatus.WAITING, PaymentPassengerStatus.WAITING, 400.0, true, -1L);
        passengerCantAfford = new Passenger(3L, user2.getEmail(), user2.getName(), user2.getSurname(), DrivePassengerStatus.ACCEPTED, PaymentPassengerStatus.WAITING, 400.0, true, -1L);
        passengerRejectedPayment = new Passenger(4L, user2.getEmail(), user2.getName(), user2.getSurname(), DrivePassengerStatus.ACCEPTED, PaymentPassengerStatus.REJECTED, 0.0, false, -1L);
        passCanAfford = new Passenger(5L, user5.getEmail(), user5.getName(), user5.getSurname(), DrivePassengerStatus.ACCEPTED, PaymentPassengerStatus.WAITING, 400.0, true, -1L);
        Passenger passAcceptedPayment = new Passenger(6L, passWithMoney.getPassengerEmail(), passWithMoney.getPassengerName(), passWithMoney.getPassengerSurname(), DrivePassengerStatus.ACCEPTED, PaymentPassengerStatus.ACCEPTED, 400.0, true, -1L);
        BankTransaction bankTransactionAcceptedPass = new BankTransaction(3L, TransactionType.OUTFLOW, passAcceptedPayment.getDebit(), TransactionStatus.FINALIZED, passWithMoney.getPassengerEmail(), "UBER");

        passengersBankTransaction = new BankTransaction(2L, TransactionType.OUTFLOW, passCanAfford.getDebit(), TransactionStatus.WAITING_FINALIZATION, passCanAfford.getPassengerEmail(), "UBER");


        Passenger passWithMoney2 = new Passenger(1L, user5.getEmail(), user5.getName(), user5.getSurname(), DrivePassengerStatus.ACCEPTED, PaymentPassengerStatus.ACCEPTED, 400.0, true, -1L);


        drive_passengerPayment_OneAccepted_OtherWaiting = new Drive(30L, new ArrayList<RealAddress>(), 10.0, 20.0, 900.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.PAYMENT_WAITING, true, new Date(), DriveType.FUTURE, 300.0, new Date(), new Date(), 1L);
        drive_passengerPayment_OneAccepted_OtherWaiting.getStops().add(stop1);
        drive_passengerPayment_OneAccepted_OtherWaiting.getPassengers().add(passWithMoney);
        drive_passengerPayment_OneAccepted_OtherWaiting.getPassengers().add(passengerCantAfford);

        drive_passengerPayment_OneAccepted_OtherWaiting2 = new Drive(30L, new ArrayList<RealAddress>(), 10.0, 20.0, 900.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.PAYMENT_WAITING, true, new Date(), DriveType.FUTURE, 300.0, new Date(), new Date(), 1L);
        drive_passengerPayment_OneAccepted_OtherWaiting2.getStops().add(stop1);
        drive_passengerPayment_OneAccepted_OtherWaiting2.getPassengers().add(passWithMoney2);
        drive_passengerPayment_OneAccepted_OtherWaiting2.getPassengers().add(passengerCantAfford);



        passengerAcceptedPaymentWaiting = new Drive(31L, new ArrayList<RealAddress>(), 10.0, 20.0, 600.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.PAYMENT_WAITING, true, new Date(), DriveType.FUTURE, 300.0, new Date(), new Date(), 1L);
        passengerAcceptedPaymentWaiting.getStops().add(stop1);
        passengerAcceptedPaymentWaiting.getPassengers().add(passWithMoney);


        //passengerAcceptedPaymentPaymentDone
        passengerAcceptedPaymentPaymentDone = new Drive(31L, new ArrayList<RealAddress>(), 10.0, 20.0, 600.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.PAYMENT_WAITING, true, new Date(), DriveType.FUTURE, 300.0, new Date(), new Date(), 1L);
        passengerAcceptedPaymentPaymentDone.getStops().add(stop1);
        passengerAcceptedPaymentPaymentDone.getPassengers().add(passWithMoney2);


        passengerAcceptedPaymentDriverWaiting = new Drive(31L, new ArrayList<RealAddress>(), 10.0, 20.0, 900.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.DRIVER_WAITING, true, new Date(), DriveType.FUTURE, 300.0, new Date(), new Date(), 1L);
        passengerAcceptedPaymentDriverWaiting.getStops().add(stop1);
        passengerAcceptedPaymentDriverWaiting.getPassengers().add(passWithMoney2);

        passengerAcceptedPaymentDriveFailed = new Drive(31L, new ArrayList<RealAddress>(), 10.0, 20.0, 900.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.DRIVE_FAILED, true, new Date(), DriveType.FUTURE, 300.0, new Date(), new Date(), 1L);
        passengerAcceptedPaymentDriveFailed.getStops().add(stop1);
        passengerAcceptedPaymentDriveFailed.getPassengers().add(passWithMoney2);

        passengerAcceptedPaymentDriverAvailable = new Drive(31L, new ArrayList<RealAddress>(), 10.0, 20.0, 900.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, driverAvailable, DriveStatus.DRIVER_WAITING, true, new Date(), DriveType.FUTURE, 300.0, new Date(), new Date(), 1L);
        passengerAcceptedPaymentDriverAvailable.getStops().add(stop1);
        passengerAcceptedPaymentDriverAvailable.getPassengers().add(passWithMoney2);



        driveNoDriver_WithPassengers = new Drive(4L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.OWNER_PAYMENT_WAITING, false, new Date(), DriveType.FUTURE, 400.0, new Date(), new Date(), -1L);
        driveNoDriver_WithPassengers.getStops().add(stop1);
        driveNoDriver_WithPassengers.getPassengers().add(passWithMoney);

        Drive driveNoDriver_WithPassengers_PassengerWaiting = new Drive(5L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.PASSENGERS_WAITING, false, new Date(), DriveType.FUTURE, 400.0, new Date(), new Date(), -1L);
        driveNoDriver_WithPassengers_PassengerWaiting.getStops().add(stop1);
        driveNoDriver_WithPassengers_PassengerWaiting.getPassengers().add(passWithMoney);

        bankTransaction = new BankTransaction(1L, TransactionType.OUTFLOW, driveNoDriver_NoPassengers.getOwnerDebit(), TransactionStatus.WAITING_VERIFICATION, driveNoDriver_NoPassengers.getOwner().getUser().getEmail(), "UBER");
        rejectedBankTransaction =  new BankTransaction(1L, TransactionType.OUTFLOW, driveNoDriver_NoPassengers.getOwnerDebit(), TransactionStatus.FAILED, driveNoDriver_NoPassengers.getOwner().getUser().getEmail(), "UBER");

        driveOwnerNoMoneyStatusOwnerWaiting = new Drive(6L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 5, 0, 0, 0, clientWithoutMoney, driveJSON, null, DriveStatus.OWNER_PAYMENT_WAITING, false, new Date(), DriveType.FUTURE, 800.0, new Date(), new Date(), -1L);
        driveOwnerNoMoneyStatusOwnerWaiting.getStops().add(stop1);
        driveOwnerNoMoneyStatusOwnerWaiting.setOwnerDebit(driveOwnerNoMoneyStatusOwnerWaiting.getPrice());

        driveOwnerNoMoneyStatusFailed = new Drive(7L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 5, 0, 0, 0, clientWithoutMoney, driveJSON, null, DriveStatus.DRIVE_FAILED, false, new Date(), DriveType.FUTURE, 800.0, new Date(), new Date(), -1L);

        driveOwnerMoneyStatusOwnerWaiting_NoDriver = new Drive(8L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.OWNER_PAYMENT_WAITING, false, new Date(), DriveType.NOW, 0.0, new Date(), new Date(), -1L);
        driveWithDriver_NoPassengers.getStops().add(stop1);
        driveNoDriver_NoPassengers.setOwnerDebit(driveNoDriver_NoPassengers.getPrice());

        //drier waiting status...no driver..drive now
        driveOwnerMoneyStatusDriverWaiting_NoDriver = new Drive(9L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.DRIVER_WAITING, false, new Date(), DriveType.NOW, 0.0, new Date(), new Date(), -1L);
        driveOwnerMoneyStatusDriverWaiting_NoDriver.getStops().add(stop1);
        driveOwnerMoneyStatusDriverWaiting_NoDriver.setOwnerDebit(driveOwnerMoneyStatusDriverWaiting_NoDriver.getPrice());


        driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered = new Drive(14L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.OWNER_PAYMENT_WAITING, true, new Date(), DriveType.NOW, 0.0, new Date(), new Date(), -1L);
        driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered.getStops().add(stop1);
        driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered.setOwnerDebit(driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered.getPrice());
        driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered.getPassengers().add(passWithMoney);


        driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered = new Drive(15L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.OWNER_PAYMENT_WAITING, true, new Date(), DriveType.NOW, 0.0, new Date(), new Date(), -1L);
        driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered.getStops().add(stop1);
        driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered.setOwnerDebit(driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered.getPrice());
        driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered.getPassengers().add(passWithMoney);


        driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited = new Drive(16L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.PASSENGERS_WAITING, false, new Date(), DriveType.NOW, 0.0, new Date(), new Date(), -1L);
        driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited.getStops().add(stop1);
        driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited.setOwnerDebit(driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited.getPrice());
        driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited.getPassengers().add(passWithMoney);



        driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited = new Drive(17L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.OWNER_PAYMENT_WAITING, false, new Date(), DriveType.NOW, 0.0, new Date(), new Date(), -1L);
        driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited.getStops().add(stop1);
        driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited.setOwnerDebit(driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited.getPrice());
        driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited.getPassengers().add(passWithMoney);


        Passenger passengerParticipationAnsweredNotPaying = new Passenger(2L, user5.getEmail(), user5.getName(), user5.getSurname(), DrivePassengerStatus.ACCEPTED, PaymentPassengerStatus.NOT_PAYING, 0.0, false, -1L);

        //prvo napraviti onog meni povoljnog passengera pa onda dodati
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture = new Drive(18L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.OWNER_PAYMENT_WAITING, false, new Date(), DriveType.FUTURE, 0.0, new Date(), new Date(), bankTransaction.getId());
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.getStops().add(stop1);
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.setOwnerDebit(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.getPrice());
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.getPassengers().add(passengerParticipationAnsweredNotPaying);

        driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture =  new Drive(19L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.DRIVER_WAITING, false, new Date(), DriveType.FUTURE, 800.0, new Date(), new Date(), bankTransaction.getId());
        driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.getStops().add(stop1);
        driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.setOwnerDebit(driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.getPrice());
        driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.getPassengers().add(passengerParticipationAnsweredNotPaying);

        driveOwnerMoney_DriveStatusDriveFailed_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture = new Drive(20L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.DRIVE_FAILED, false, new Date(), DriveType.FUTURE, 800.0, new Date(), new Date(), bankTransaction.getId());
        driveOwnerMoney_DriveStatusDriveFailed_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.getStops().add(stop1);
        driveOwnerMoney_DriveStatusDriveFailed_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.setOwnerDebit(driveOwnerMoney_DriveStatusDriveFailed_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.getPrice());
        driveOwnerMoney_DriveStatusDriveFailed_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.getPassengers().add(passengerParticipationAnsweredNotPaying);


        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow = new Drive(21L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.OWNER_PAYMENT_WAITING, false, new Date(), DriveType.NOW, 800.0, new Date(), new Date(), bankTransaction.getId());
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.getStops().add(stop1);
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.setOwnerDebit(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.getPrice());
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.getPassengers().add(passengerParticipationAnsweredNotPaying);

        driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow =  new Drive(22L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.DRIVER_WAITING, false, new Date(), DriveType.NOW, 800.0, new Date(), new Date(), bankTransaction.getId());
        driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.getStops().add(stop1);
        driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.setOwnerDebit(driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.getPrice());
        driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.getPassengers().add(passengerParticipationAnsweredNotPaying);

        Drive driveOwnerMoney_DriveStatusDriveFailed_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow = new Drive(23L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.DRIVE_FAILED, false, new Date(), DriveType.NOW, 800.0, new Date(), new Date(), bankTransaction.getId());
        driveOwnerMoney_DriveStatusDriveFailed_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.getStops().add(stop1);
        driveOwnerMoney_DriveStatusDriveFailed_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.setOwnerDebit(driveOwnerMoney_DriveStatusDriveFailed_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.getPrice());
        driveOwnerMoney_DriveStatusDriveFailed_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.getPassengers().add(passengerParticipationAnsweredNotPaying);

//payment accepted - owner accepted - has passengers - participation answered - bill splited - pass cannot afford
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAfford = new Drive(24L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.OWNER_PAYMENT_WAITING, true, new Date(), DriveType.NOW, 400.0, new Date(), new Date(), -1L);
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAfford.getStops().add(stop1);
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAfford.setOwnerDebit(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAfford.getPrice());
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAfford.getPassengers().add(passengerCantAfford);

        //payment accepted - owner accepted - has passengers - participation answered - bill splited - pass cannot afford --- passenger rejected from payment
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected = new Drive(25L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.OWNER_PAYMENT_WAITING, true, new Date(), DriveType.NOW, 400.0, new Date(), new Date(), -1L);
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected.getStops().add(stop1);
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected.getPassengers().add(passengerRejectedPayment);

        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected_NewPrice = new Drive(25L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.OWNER_PAYMENT_WAITING, true, new Date(), DriveType.NOW, 800.0, new Date(), new Date(), -1L);
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected_NewPrice.getStops().add(stop1);
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected_NewPrice.getPassengers().add(passengerRejectedPayment);


        driveOwnerMoney_DriveStatus_OwnerPaymentWaiting_WithPassengers_ParticipationAnswered_PassengersCanAfford = new Drive(26L, new ArrayList<RealAddress>(), 20.0, 30.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.OWNER_PAYMENT_WAITING, true,new Date(), DriveType.NOW, 400.0, new Date(), new Date(), bankTransaction.getId());
        driveOwnerMoney_DriveStatus_OwnerPaymentWaiting_WithPassengers_ParticipationAnswered_PassengersCanAfford.getStops().add(stop1);
        driveOwnerMoney_DriveStatus_OwnerPaymentWaiting_WithPassengers_ParticipationAnswered_PassengersCanAfford.getPassengers().add(passCanAfford);

        driveOwnerMoney_DriveStatus_PaymentWaiting_WithPassengers_ParticipationAnswered_PassengersCanAfford = new Drive(27L, new ArrayList<RealAddress>(), 20.0, 30.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.PAYMENT_WAITING, true,new Date(), DriveType.NOW, 400.0, new Date(), new Date(), bankTransaction.getId());
        driveOwnerMoney_DriveStatus_PaymentWaiting_WithPassengers_ParticipationAnswered_PassengersCanAfford.getStops().add(stop1);
        driveOwnerMoney_DriveStatus_PaymentWaiting_WithPassengers_ParticipationAnswered_PassengersCanAfford.getPassengers().add(passCanAfford);


        driveOwnerMoneyStatusDriverWaiting_NoDriver_future = new Drive(10L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.DRIVER_WAITING, false, new Date(), DriveType.FUTURE, 0.0, new Date(), new Date(), -1L);
        driveOwnerMoneyStatusDriverWaiting_NoDriver_future.getStops().add(stop1);
        driveOwnerMoneyStatusDriverWaiting_NoDriver_future.setOwnerDebit(driveOwnerMoneyStatusDriverWaiting_NoDriver_future.getPrice());


        driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive = new Drive(11L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.OWNER_PAYMENT_WAITING, false, new Date(), DriveType.FUTURE, 0.0, new Date(), new Date(), -1L);
        driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getStops().add(stop1);
        driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.setOwnerDebit(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getPrice());

        driveOwnerMoneyStatusDriverWaiting_NoDriver_futureDrive = new Drive(12L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, null, DriveStatus.DRIVER_WAITING, false, new Date(), DriveType.FUTURE, 0.0, new Date(), new Date(), -1L);
        driveOwnerMoneyStatusDriverWaiting_NoDriver_futureDrive.getStops().add(stop1);
        driveOwnerMoneyStatusDriverWaiting_NoDriver_futureDrive.setOwnerDebit(driveOwnerMoneyStatusDriverWaiting_NoDriver_futureDrive.getPrice());


        driveWithAvailableDriver = new Drive(13L, new ArrayList<RealAddress>(), 10.0, 20.0, 800.0, new HashSet<Passenger>(), 4, 0, 0, 0, clientWithMoney, driveJSON, driverAvailable, DriveStatus.DRIVER_WAITING, false, new Date(), DriveType.FUTURE, 0.0, new Date(), new Date(), -1L);
        driveWithAvailableDriver.getStops().add(stop1);
        driveWithAvailableDriver.setOwnerDebit(driveWithAvailableDriver.getPrice());

    }



    @Test
    public void driveStatusShouldBeFail(){
        failedDrive.setId(driveNoDriver_NoPassengers.getId());
        Mockito.doReturn(failedDrive).when(driveRepository).save(driveNoDriver_NoPassengers);

        driveNoDriver_NoPassengers = driveService.driveFailed(driveNoDriver_NoPassengers);

        assertEquals(DriveStatus.DRIVE_FAILED, driveNoDriver_NoPassengers.getDriveStatus());
        verify(driveRepository, times(1)).save(any());


    }



    @Test
    public void saveDriveOwnerHasMoney() throws EmailNotFoundException {
        driveNoDriver_NoPassengers.setId(driveNoDriver_NoPassengers.getId());


        Mockito.doReturn(true).when(userService).canAffordDrive(driveNoDriver_NoPassengers.getOwner().getUser().getEmail(), driveNoDriver_NoPassengers.getOwnerDebit());
        Mockito.doReturn(bankTransaction).when(bankService).requestOwnerPayment(driveNoDriver_NoPassengers);
        Mockito.doReturn(driveNoDriver_NoPassengers).when(driveRepository).save(driveNoDriver_NoPassengers);

        driveNoDriver_NoPassengers = driveService.saveDrive(driveNoDriver_NoPassengers);


        assertEquals(driveNoDriver_NoPassengers.getDriveStatus(), DriveStatus.OWNER_PAYMENT_WAITING);
        assertEquals(driveNoDriver_NoPassengers.getOwnerTransactionId(), bankTransaction.getId());

        verify(userService, times(1)).canAffordDrive(driveNoDriver_NoPassengers.getOwner().getUser().getEmail(), driveNoDriver_NoPassengers.getOwnerDebit());
        verify(bankService, times(1)).requestOwnerPayment(any());
        verify(driveRepository, times(1)).save(any());
    }



    //nema para
    //menja se
    @Test
    public void doNotSaveDriveOwnerIsPoor() throws EmailNotFoundException {
        driveOwnerNoMoneyStatusFailed.setId(driveOwnerNoMoneyStatusOwnerWaiting.getId());

        Mockito.doReturn(false).when(userService).canAffordDrive(driveOwnerNoMoneyStatusOwnerWaiting.getOwner().getUser().getEmail(), driveOwnerNoMoneyStatusOwnerWaiting.getOwnerDebit());
        Mockito.doNothing().when(notificationService).paymentFailedDriveCanceledNotify(driveOwnerNoMoneyStatusOwnerWaiting.getOwner().getUser().getEmail());
        Mockito.doReturn(driveOwnerNoMoneyStatusFailed).when(driveRepository).save(driveOwnerNoMoneyStatusOwnerWaiting);

        driveOwnerNoMoneyStatusOwnerWaiting = driveService.saveDrive(driveOwnerNoMoneyStatusOwnerWaiting);

        assertEquals( DriveStatus.DRIVE_FAILED, driveOwnerNoMoneyStatusOwnerWaiting.getDriveStatus());
        verify(userService, times(1)).canAffordDrive(driveOwnerNoMoneyStatusOwnerWaiting.getOwner().getUser().getEmail(), driveOwnerNoMoneyStatusOwnerWaiting.getOwnerDebit());
        verify(notificationService, times(1)).paymentFailedDriveCanceledNotify(driveOwnerNoMoneyStatusOwnerWaiting.getOwner().getUser().getEmail());
        verify(driveRepository, times(1)).save(any());
    }

    //baci exc prosledi rend mejl --> can afford drive se trigeruje
    @Test
    public void saveDriveEmailNotFound() throws EmailNotFoundException {
        Mockito.doReturn(null).when(clientsRepository).findClientsAccountByUserEmail(driveOwnerNoMoneyStatusOwnerWaiting.getOwner().getUser().getEmail());

        Mockito.doThrow(EmailNotFoundException.class).when(userService).canAffordDrive(driveOwnerNoMoneyStatusOwnerWaiting.getOwner().getUser().getEmail(), driveOwnerNoMoneyStatusOwnerWaiting.getOwnerDebit());

        assertThrows(EmailNotFoundException.class, ()->{
            driveService.saveDrive(driveOwnerNoMoneyStatusOwnerWaiting);
        });
    }




//payment accepted fja ce istestirati drive waiting for passengers


    @Test
    public void paymentDoneDriverNotAvailableDriveFailed_DriveNow() throws URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException, EmailNotFoundException {
        List<DriversAccount> empty = new ArrayList<DriversAccount>();
        driveOwnerMoneyStatusDriverWaiting_NoDriver.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getId());
        driveOwnerMoneyStatusDriverWaiting_NoDriver_future.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getId());
        failedDrive.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getId());

        Mockito.doReturn(driveOwnerMoneyStatusDriverWaiting_NoDriver).when(driveRepository).save(driveOwnerMoneyStatusOwnerWaiting_NoDriver);
        Mockito.doReturn(driveOwnerMoneyStatusDriverWaiting_NoDriver_future).when(driveRepository).save(driveOwnerMoneyStatusDriverWaiting_NoDriver);
        Mockito.doReturn(empty).when(userService).getDriversByStatusAndAvailability(true, DriverStatus.AVAILABLE);
        Mockito.doReturn(empty).when(userService).getDriversByStatusAndAvailability(true, DriverStatus.BUSY);
        Mockito.doReturn(failedDrive).when(driveRepository).save(driveOwnerMoneyStatusDriverWaiting_NoDriver_future);

        driveOwnerMoneyStatusOwnerWaiting_NoDriver = driveService.paymentDone(driveOwnerMoneyStatusOwnerWaiting_NoDriver);

        assertEquals(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getDriveStatus(), DriveStatus.DRIVE_FAILED);
        assertNull(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getDriver());
        verify(driveRepository, times(3)).save(any());
        verify(userService, times(1)).getDriversByStatusAndAvailability(true,DriverStatus.AVAILABLE);
        verify(userService, times(1)).getDriversByStatusAndAvailability(true,DriverStatus.BUSY);
        verify(notificationService, times(1)).addNotificationMultiple(any(), any());
        verify(bankService, times(1)).declineTransaction(any(), any());
    }

    @Test
    public void paymentDoneDriverNotAvailableDriveFailed_DriveFuture() throws URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException, EmailNotFoundException {
        List<DriversAccount> empty = new ArrayList<DriversAccount>();
        driveOwnerMoneyStatusDriverWaiting_NoDriver_futureDrive.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getId());
        failedDrive.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getId());


        Mockito.doReturn(driveOwnerMoneyStatusDriverWaiting_NoDriver_futureDrive).when(driveRepository).save(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive);
        Mockito.doReturn(empty).when(userService).getDrivers();
        Mockito.doReturn(failedDrive).when(driveRepository).save(driveOwnerMoneyStatusDriverWaiting_NoDriver_futureDrive);


        driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive = driveService.paymentDone(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive);

        assertEquals(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getDriveStatus(), DriveStatus.DRIVE_FAILED);
        assertNull(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getDriver());
        verify(driveRepository, times(2)).save(any());
        verify(userService, never()).getDriversByStatusAndAvailability(true, DriverStatus.AVAILABLE); //one metode su privatne .. znamo samo da kad trazi future da nece traziti trenutnu distancu
        verify(notificationService, times(1)).addNotificationMultiple(any(), any());
        verify(bankService, times(1)).declineTransaction(any(), any());

    }

    //transactionid does not exist
    @Test
    public void paymentDone_DriveFailedTransactionCancel_IdDoesNotExistException() throws TransactionIdDoesNotExistException {
        driveOwnerMoneyStatusDriverWaiting_NoDriver_futureDrive.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getId());
        Mockito.doReturn(driveOwnerMoneyStatusDriverWaiting_NoDriver_futureDrive).when(driveRepository).save(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive);
        Mockito.doThrow(TransactionIdDoesNotExistException.class).when(bankService).declineTransaction(any(), any());

        assertThrows(TransactionIdDoesNotExistException.class, ()->{
            driveService.paymentDone(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive);
        });
    }

    @Test
    public void paymentDone_DriverAvailable_DriveFuture() throws URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException, EmailNotFoundException {
        List<DriversAccount> drivers = new ArrayList<DriversAccount>();
        drivers.add(driverAvailable);

        driveOwnerMoneyStatusDriverWaiting_NoDriver_futureDrive.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getId());
        driveWithAvailableDriver.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getId());

        Mockito.doReturn(driveOwnerMoneyStatusDriverWaiting_NoDriver_futureDrive).when(driveRepository).save(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive);
        Mockito.doReturn(drivers).when(userService).getDrivers();
        Mockito.doReturn(driverAvailable).when(userService).getDriver(driverAvailable.getUser().getEmail());
        Mockito.doReturn(driveWithAvailableDriver).when(driveRepository).save(driveOwnerMoneyStatusDriverWaiting_NoDriver_futureDrive);

        driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive = driveService.paymentDone(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive);

        assertEquals(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getDriver(), driverAvailable);
        assertEquals(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getDriveStatus(), DriveStatus.DRIVER_WAITING);

        verify(userService, times(1)).getDrivers();
        verify(userService, times(1)).getDriver((String) any());
        verify(notificationService, times(1)).addNotification(any());
        verify(notificationService, times(1)).addNotificationMultiple(any(), any());
        verify(driveRepository, times(2)).save(any());
    }

    @Test
    public void paymentDone_DriverAvailable_DriveNow() throws URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException, EmailNotFoundException {
        List<DriversAccount> drivers = new ArrayList<DriversAccount>();
        drivers.add(driverAvailable);
        driveOwnerMoneyStatusDriverWaiting_NoDriver.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getId());
        driveOwnerMoneyStatusDriverWaiting_NoDriver_future.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getId());
        driverAvailable.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getId());


        Mockito.doReturn(driveOwnerMoneyStatusDriverWaiting_NoDriver).when(driveRepository).save(driveOwnerMoneyStatusOwnerWaiting_NoDriver);
        Mockito.doReturn(driveOwnerMoneyStatusDriverWaiting_NoDriver_future).when(driveRepository).save(driveOwnerMoneyStatusDriverWaiting_NoDriver);
        Mockito.doReturn(drivers).when(userService).getDriversByStatusAndAvailability(true, DriverStatus.AVAILABLE);
        Mockito.doReturn(driverAvailable).when(userService).getDriver(driverAvailable.getUser().getEmail());
        Mockito.doReturn(driveWithAvailableDriver).when(driveRepository).save(driveOwnerMoneyStatusDriverWaiting_NoDriver_future);

        driveOwnerMoneyStatusOwnerWaiting_NoDriver = driveService.paymentDone(driveOwnerMoneyStatusOwnerWaiting_NoDriver);


        assertEquals(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getDriveStatus(), DriveStatus.DRIVER_WAITING);
        assertEquals(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getDriver(), driverAvailable);

        verify(driveRepository, times(3)).save(any());
        verify(userService, times(1)).getDriversByStatusAndAvailability(true, DriverStatus.AVAILABLE);
        verify(userService, never()).getDriversByStatusAndAvailability(true, DriverStatus.BUSY);
        verify(userService, times(1)).getDriver((String) any());
        verify(notificationService, times(1)).addNotification(any());
        verify(notificationService, times(1)).addNotificationMultiple(any(), any());

    }


    //payment accepted - throw not found
    @Test
    public void paymentAccepted_DriveForTransactionDoesNotExistException(){
        Mockito.doReturn(null).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(any(), any());

        assertThrows(NotFoundException.class, ()->{
            driveService.paymentAccepted(bankTransaction);
        });
    }


    //payment accepted - owner - without pass - no driver - drive now
    @Test
    public void paymentAccepted_OwnerPaymentAccepted_NoDriver_NoPassengers_DriveNow() throws EmailNotFoundException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        driveOwnerMoneyStatusDriverWaiting_NoDriver.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getId());
        driveOwnerMoneyStatusDriverWaiting_NoDriver_future.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getId());
        failedDrive.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getId());

        Mockito.doReturn(driveOwnerMoneyStatusOwnerWaiting_NoDriver).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getOwner().getUser().getEmail(), bankTransaction.getId());
        Mockito.doReturn(driveOwnerMoneyStatusOwnerWaiting_NoDriver).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getOwner().getUser().getEmail(), bankTransaction.getId());

        Mockito.doReturn(driveOwnerMoneyStatusDriverWaiting_NoDriver).when(driveRepository).save(driveOwnerMoneyStatusOwnerWaiting_NoDriver);
        Mockito.doReturn(driveOwnerMoneyStatusDriverWaiting_NoDriver_future).when(driveRepository).save(driveOwnerMoneyStatusDriverWaiting_NoDriver);
        Mockito.doReturn(failedDrive).when(driveRepository).save(driveOwnerMoneyStatusDriverWaiting_NoDriver_future);

        driveService.paymentAccepted(bankTransaction);

        verify(driveRepository, times(2)).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        verify(userService, times(1)).getDriversByStatusAndAvailability(true, DriverStatus.AVAILABLE);
        verify(userService, times(1)).getDriversByStatusAndAvailability(true, DriverStatus.BUSY);
        verify(notificationService, times(1)).addNotificationMultiple(any(), any());
        verify(bankService, times(1)).declineTransaction(any(), any());
        verify(driveRepository, times(3)).save(any());

    }

    //payment accepted - owner - without pass - no driver - drive future
    @Test
    public void paymentAccepted_OwnerPaymentAccepted_NoDriver_NoPassengers_DriveFuture() throws EmailNotFoundException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        List<DriversAccount> empty = new ArrayList<DriversAccount>();
        driveOwnerMoneyStatusDriverWaiting_NoDriver_future.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getId());
        failedDrive.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getId());

        Mockito.doReturn(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getOwner().getUser().getEmail(), bankTransaction.getId());
        Mockito.doReturn(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getOwner().getUser().getEmail(), bankTransaction.getId());

        Mockito.doReturn(driveOwnerMoneyStatusDriverWaiting_NoDriver_future).when(driveRepository).save(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive);
        Mockito.doReturn(empty).when(userService).getDrivers();
        Mockito.doReturn(failedDrive).when(driveRepository).save(driveOwnerMoneyStatusDriverWaiting_NoDriver_future);


        driveService.paymentAccepted(bankTransaction);

        verify(driveRepository, times(2)).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        verify(userService, times(1)).getDrivers();
        verify(notificationService, times(1)).addNotificationMultiple(any(), any());
        verify(bankService, times(1)).declineTransaction(any(), any());
        verify(driveRepository, times(2)).save(any());

    }

    //payment accepted - owner - without pass - nasao driver - future
    @Test
    public void paymentAccepted_OwnerPaymentAccepted_Driver_NoPassengers_DriveFuture() throws EmailNotFoundException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        List<DriversAccount> drivers = new ArrayList<DriversAccount>();
        drivers.add(driverAvailable);

        driveOwnerMoneyStatusDriverWaiting_NoDriver_future.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getId());
        driveWithAvailableDriver.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getId());

        Mockito.doReturn(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getOwner().getUser().getEmail(), bankTransaction.getId());
        Mockito.doReturn(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive.getOwner().getUser().getEmail(), bankTransaction.getId());
        Mockito.doReturn(driveOwnerMoneyStatusDriverWaiting_NoDriver_future).when(driveRepository).save(driveOwnerMoneyStatusOwnerWaiting_NoDriver_futureDrive);
        Mockito.doReturn(drivers).when(userService).getDrivers();
        Mockito.doReturn(driverAvailable).when(userService).getDriver(driverAvailable.getUser().getEmail());
        Mockito.doReturn(driveWithAvailableDriver).when(driveRepository).save(driveOwnerMoneyStatusDriverWaiting_NoDriver_future);

        driveService.paymentAccepted(bankTransaction);


        verify(driveRepository, times(2)).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        verify(userService, times(1)).getDriver(driverAvailable.getUser().getEmail());
        verify(notificationService, times(1)).addNotification(any());
        verify(notificationService, times(1)).addNotificationMultiple(any(), any());
        verify(driveRepository, times(2)).save(any());
    }

    //payment accepted - owner - without pass - nasao driver - now
    @Test
    public void paymentAccepted_OwnerPaymentAccepted_Driver_NoPassengers_DriveNow() throws EmailNotFoundException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        List<DriversAccount> drivers = new ArrayList<DriversAccount>();
        drivers.add(driverAvailable);

        driveOwnerMoneyStatusDriverWaiting_NoDriver.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getId());
        driveOwnerMoneyStatusDriverWaiting_NoDriver_future.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getId());
        driveWithAvailableDriver.setId(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getId());

        Mockito.doReturn(driveOwnerMoneyStatusOwnerWaiting_NoDriver).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getOwner().getUser().getEmail(), bankTransaction.getId());
        Mockito.doReturn(driveOwnerMoneyStatusOwnerWaiting_NoDriver).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(driveOwnerMoneyStatusOwnerWaiting_NoDriver.getOwner().getUser().getEmail(), bankTransaction.getId());
        Mockito.doReturn(driveOwnerMoneyStatusDriverWaiting_NoDriver).when(driveRepository).save(driveOwnerMoneyStatusOwnerWaiting_NoDriver);
        Mockito.doReturn(driveOwnerMoneyStatusDriverWaiting_NoDriver_future).when(driveRepository).save(driveOwnerMoneyStatusDriverWaiting_NoDriver);
        Mockito.doReturn(drivers).when(userService).getDriversByStatusAndAvailability(true, DriverStatus.AVAILABLE);
        Mockito.doReturn(driverAvailable).when(userService).getDriver(driverAvailable.getUser().getEmail());
        Mockito.doReturn(driveWithAvailableDriver).when(driveRepository).save(driveOwnerMoneyStatusDriverWaiting_NoDriver_future);

        driveService.paymentAccepted(bankTransaction);

        verify(driveRepository, times(2)).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        verify(userService, times(1)).getDriversByStatusAndAvailability(true, DriverStatus.AVAILABLE);
        verify(notificationService, times(1)).addNotification(any());
        verify(notificationService, times(1)).addNotificationMultiple(any(), any());
        verify(driveRepository, times(3)).save(any());

    }

    //owner payment accepted with passengers participation not answred bill splited
    @Test
    public void paymentAccepted_OwnerPaymentAccepted_WithPassengers_ParticipationNotAnswered() throws EmailNotFoundException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        String sender = driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered.getOwner().getUser().getEmail();
        driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered.setOwnerTransactionId(bankTransaction.getId());
        driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered.setId(driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered.getId());

        Mockito.doReturn(driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(sender, bankTransaction.getId());
        Mockito.doReturn(driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(sender, bankTransaction.getId());
        Mockito.doReturn(driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered).when(driveRepository).save(driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered);

        driveService.paymentAccepted(bankTransaction);
        verify(driveRepository, times(2)).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        verify(notificationService, times(1)).addedToDriveNotify(driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered.getPassengers(), driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered.getId());
        verify(driveRepository, times(1)).save(any());
    }

    //owner payment accepted with passengers participation not answred bill is not  splited
    @Test
    public void paymentAccepted_OwnerPaymentAccepted_WithPassengers_ParticipationNotAnswered_BillNotSplit() throws EmailNotFoundException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        String sender = driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited.getOwner().getUser().getEmail();
        driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited.setOwnerTransactionId(bankTransaction.getId());
        driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited.setId(driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited.getId());

        Mockito.doReturn(driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(sender, bankTransaction.getId());
        Mockito.doReturn(driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(sender, bankTransaction.getId());
        Mockito.doReturn(driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited).when(driveRepository).save(driveOwnerMoneyStatusOwnerPaymentWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited);

        driveService.paymentAccepted(bankTransaction);

        verify(driveRepository, times(2)).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        verify(notificationService, times(1)).addedToDriveNotify(driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited.getPassengers(), driveOwnerMoneyStatusPassengersWaiting_NoDriver_withPassenger_ParticipationNotAnswered_billNotSplited.getId());
        verify(driveRepository, times(1)).save(any());

    }

    //payment accepted - owner accepted - has passengers - participation is answered - bil nije splitovan -- owner samo placa -- drive future
    @Test
    public void paymentAccepted_OwnerPaymentAccepted_HasPassengers_ParticipationIsAnswered_BillNotSplit_ShouldBePaymentDone_FutureDrive_NoDriverFound() throws EmailNotFoundException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        String sender = driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.getOwner().getUser().getEmail();
        driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.setId(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.getId());
        driveOwnerMoney_DriveStatusDriveFailed_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.setId(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.getId());

        Mockito.doReturn(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(sender, bankTransaction.getId());
        Mockito.doReturn(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(sender, bankTransaction.getId());
        Mockito.doReturn(driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture).when(driveRepository).save(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture);
        Mockito.doReturn(new ArrayList<DriversAccount>()).when(userService).getDrivers();
        Mockito.doReturn(driveOwnerMoney_DriveStatusDriveFailed_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture).when(driveRepository).save(driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture);

        this.driveService.paymentAccepted(bankTransaction);

        verify(driveRepository, times(2)).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        verify(userService, times(1)).getDrivers();
        verify(notificationService, times(1)).addNotificationMultiple(any(), any());
        verify(bankService, times(1)).declineTransaction(any(), any());
        verify(driveRepository, times(2)).save(any());
    }

    //payment accepted - owner accepted - has passengers - participation is answered - bil nije splitovan -- owner samo placa -- drive now
    @Test
    public void paymentAccepted_OwnerPaymentAccepted_HasPassengers_ParticipationIsAnswered_BillNotSplit_ShouldBePaymentDone_DriveNow_NoDriverFound() throws EmailNotFoundException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        String sender = driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.getOwner().getUser().getEmail();
        driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.setId(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.getId());
        driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.setId(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.getId());
        driveOwnerMoney_DriveStatusDriveFailed_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture.setId(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow.getId());

        Mockito.doReturn(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(sender, bankTransaction.getId());
        Mockito.doReturn(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(sender, bankTransaction.getId());
        Mockito.doReturn(driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow).when(driveRepository).save(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow);
        Mockito.doReturn(driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture).when(driveRepository).save(driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveNow);
        Mockito.doReturn(new ArrayList<DriversAccount>()).when(userService).getDriversByStatusAndAvailability(true, DriverStatus.AVAILABLE);
        Mockito.doReturn(new ArrayList<DriversAccount>()).when(userService).getDriversByStatusAndAvailability(true, DriverStatus.BUSY);
        Mockito.doReturn(driveOwnerMoney_DriveStatusDriveFailed_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture).when(driveRepository).save(driveOwnerMoney_DriveStatusDriverWaiting_WithPassengers_ParticipationAccepted_PassengersNotPayingBillNotSplit_NoDriverFound_DriveFuture);

        this.driveService.paymentAccepted(bankTransaction);

        verify(driveRepository, times(2)).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        verify(userService, times(1)).getDriversByStatusAndAvailability(true, DriverStatus.AVAILABLE);
        verify(userService, times(1)).getDriversByStatusAndAvailability(true, DriverStatus.BUSY);
        verify(notificationService, times(1)).addNotificationMultiple(any(), any());
        verify(bankService, times(1)).declineTransaction(any(), any());
        verify(driveRepository, times(3)).save(any());
    }


    //payment accepted - owner accepted - has passengers - participation answered - bill splited - pass cannot afford
    @Test
    public void paymentAccepted_OwnerPaymentAccepted_WithPassengers_ParticipationAnswered_BillSplit_PassengerCantAfford() throws EmailNotFoundException, TransactionIdDoesNotExistException, URISyntaxException, IOException, InterruptedException {
        String sender = driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAfford.getOwner().getUser().getEmail();
        passengerRejectedPayment.setId(passengerCantAfford.getId());
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected.setId(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAfford.getId());
        driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected_NewPrice.setId(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAfford.getId());

        Mockito.doReturn(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAfford).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(sender, bankTransaction.getId());
        Mockito.doReturn(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAfford).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(sender, bankTransaction.getId());
        Mockito.doReturn(clientWithoutMoney).when(userService).findClientsAccount(passengerCantAfford.getPassengerEmail());
        Mockito.doReturn(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected).when(driveRepository).save(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAfford);
        Mockito.doReturn(null).when(bankService).declineTransaction(bankTransaction.getId(), driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected.getOwner().getClientsBankAccount().getAccountNumber());
        Mockito.doReturn(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected_NewPrice).when(driveRepository).save(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected);
        Mockito.doReturn(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected_NewPrice).when(driveRepository).save(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected_NewPrice);
        Mockito.doReturn(bankTransaction).when(bankService).requestOwnerPayment(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected_NewPrice);

        this.driveService.paymentAccepted(bankTransaction);

        verify(driveRepository, times(2)).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        verify(userService, times(2)).findClientsAccount(passengerCantAfford.getPassengerEmail());
        verify(bankService, times(1)).declineTransaction(any(), any());
        verify(bankService, times(1)).requestOwnerPayment(driveOwnerMoney_DriveStatusOwnerPaymentWaiting_WithPassengers_ParticipationAnswered_BillIsSplit_passengerCantAffordRejected_NewPrice);
        verify(driveRepository, times(2)).save(any());
    }

    //payment accepted - owner accepted - has passengers - participation answered - bill splited - pas can afford - svi pass su waiting (posto je owner odg -- svi su waiting za payment svakako..tek ce se pitati sad)
    @Test
    public void paymentAccepted_OwnerPaymentAccepted_WithPassengers_ParticipationAnswered_BillSplit_PassCanAfford() throws EmailNotFoundException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        String sender = driveOwnerMoney_DriveStatus_OwnerPaymentWaiting_WithPassengers_ParticipationAnswered_PassengersCanAfford.getOwner().getUser().getEmail();
        driveOwnerMoney_DriveStatus_PaymentWaiting_WithPassengers_ParticipationAnswered_PassengersCanAfford.setId(driveOwnerMoney_DriveStatus_OwnerPaymentWaiting_WithPassengers_ParticipationAnswered_PassengersCanAfford.getId());

        Mockito.doReturn(driveOwnerMoney_DriveStatus_OwnerPaymentWaiting_WithPassengers_ParticipationAnswered_PassengersCanAfford).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(sender, bankTransaction.getId());
        Mockito.doReturn(driveOwnerMoney_DriveStatus_OwnerPaymentWaiting_WithPassengers_ParticipationAnswered_PassengersCanAfford).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(sender, bankTransaction.getId());
        Mockito.doReturn(clientAccPassCanAfford).when(userService).findClientsAccount(passCanAfford.getPassengerEmail());
        Mockito.doReturn(passengersBankTransaction).when(bankService).requestPassengerPayment(driveOwnerMoney_DriveStatus_OwnerPaymentWaiting_WithPassengers_ParticipationAnswered_PassengersCanAfford, clientAccPassCanAfford);
        Mockito.doReturn(driveOwnerMoney_DriveStatus_PaymentWaiting_WithPassengers_ParticipationAnswered_PassengersCanAfford).when(driveRepository).save(driveOwnerMoney_DriveStatus_OwnerPaymentWaiting_WithPassengers_ParticipationAnswered_PassengersCanAfford);

        this.driveService.paymentAccepted(bankTransaction);

        verify(driveRepository, times(2)).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        verify(userService, times(2)).findClientsAccount(any());
        verify(bankService, times(1)).requestPassengerPayment(any(), any());
        verify(driveRepository, times(1)).save(any());
    }


    //payment accepted - passenger payment accepted - payment not answered
    @Test
    public void paymentAccepted_PassengerPaymentAccepted_PaymentNotAnswered() throws EmailNotFoundException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        passWithMoney.setTransactionId(2L);
        passWithMoney.setDebit(300.0);
        passengerCantAfford.setDebit(300.0);
        passWithMoney.setPayment(PaymentPassengerStatus.WAITING);
        passWithMoney.setContribution(DrivePassengerStatus.ACCEPTED);
        passengerCantAfford.setContribution(DrivePassengerStatus.ACCEPTED);
        passengerCantAfford.setPayment(PaymentPassengerStatus.WAITING);
        List<Drive> drives = new ArrayList<>();
        drives.add(drive_passengerPayment_OneAccepted_OtherWaiting);
        drives.add(driveNoDriver_NoPassengers);

        String ownerEmail = drive_passengerPayment_OneAccepted_OtherWaiting.getOwner().getUser().getEmail();

        Mockito.doReturn(null).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(ownerEmail, passengersBankTransaction.getId());
        Mockito.doReturn(drives).when(driveRepository).findAll();
        Mockito.doReturn(drive_passengerPayment_OneAccepted_OtherWaiting2).when(driveRepository).save(drive_passengerPayment_OneAccepted_OtherWaiting);
        Mockito.doReturn(drive_passengerPayment_OneAccepted_OtherWaiting2).when(driveRepository).save(drive_passengerPayment_OneAccepted_OtherWaiting2);

        driveService.paymentAccepted(passengersBankTransaction);

        verify(driveRepository, times(1)).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        verify(driveRepository, times(1)).findAll();
        verify(driveRepository, times(2)).save(any());


    }
    //payment accepted - pass payment accepted - payment answered -- all accepted -- drive future -failed
    @Test
    public void paymentAccepted_passengerPaymentAccepted_DriveFailed() throws TransactionIdDoesNotExistException, EmailNotFoundException, URISyntaxException, IOException, InterruptedException {
        passWithMoney.setTransactionId(2L);
        passWithMoney.setDebit(300.0);
        passengerCantAfford.setDebit(300.0);
        passWithMoney.setPayment(PaymentPassengerStatus.WAITING);
        passWithMoney.setContribution(DrivePassengerStatus.ACCEPTED);
        List<Drive> drives = new ArrayList<>();
        drives.add(driveNoDriver_NoPassengers);
        drives.add(passengerAcceptedPaymentWaiting);

        String ownerEmail = passengerAcceptedPaymentWaiting.getOwner().getUser().getEmail();

        Mockito.doReturn(null).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(ownerEmail, passengerAcceptedPaymentWaiting.getOwnerTransactionId());
        Mockito.doReturn(drives).when(driveRepository).findAll();
        Mockito.doReturn(passengerAcceptedPaymentPaymentDone).when(driveRepository).save(passengerAcceptedPaymentWaiting);
        Mockito.doReturn(passengerAcceptedPaymentDriverWaiting).when(driveRepository).save(passengerAcceptedPaymentPaymentDone);
        Mockito.doReturn(new ArrayList<>()).when(userService).getDrivers();

        Mockito.doReturn(passengerAcceptedPaymentDriveFailed).when(driveRepository).save(passengerAcceptedPaymentDriverWaiting);
        Mockito.doReturn(passengerAcceptedPaymentDriveFailed).when(driveRepository).save(passengerAcceptedPaymentDriveFailed);

        driveService.paymentAccepted(passengersBankTransaction);


        verify(driveRepository, times(1)).findByOwner_User_EmailAndOwnerTransactionId(any(),  any());
        verify(driveRepository,times(1)).findAll();
        verify(driveRepository, times(4)).save(any());
        verify(userService, times(1)).getDrivers();

    }


    //payment accepted - pass payment accepted - payment answered -- all accepted -- drive future - driver available
    @Test
    public void paymentAccepted_passengerPaymentAccepted_DriverAvailable() throws EmailNotFoundException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        passWithMoney.setTransactionId(2L);
        passWithMoney.setDebit(300.0);
        passengerCantAfford.setDebit(300.0);
        passWithMoney.setPayment(PaymentPassengerStatus.WAITING);
        passWithMoney.setContribution(DrivePassengerStatus.ACCEPTED);

        List<Drive> drives = new ArrayList<>();
        drives.add(driveNoDriver_NoPassengers);
        drives.add(passengerAcceptedPaymentWaiting);

        List<DriversAccount> drivers = new ArrayList<>();
        drivers.add(driverAvailable);

        String ownerEmail = passengerAcceptedPaymentWaiting.getOwner().getUser().getEmail();

        Mockito.doReturn(null).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(ownerEmail, passengerAcceptedPaymentWaiting.getOwnerTransactionId());
        Mockito.doReturn(drives).when(driveRepository).findAll();
        Mockito.doReturn(passengerAcceptedPaymentPaymentDone).when(driveRepository).save(passengerAcceptedPaymentWaiting);
        Mockito.doReturn(passengerAcceptedPaymentDriverWaiting).when(driveRepository).save(passengerAcceptedPaymentPaymentDone);
        Mockito.doReturn(drivers).when(userService).getDrivers();
        Mockito.doReturn(driverAvailable).when(userService).getDriver(driverAvailable.getUser().getEmail());
        Mockito.doReturn(passengerAcceptedPaymentDriverAvailable).when(driveRepository).save(passengerAcceptedPaymentDriverWaiting);
        Mockito.doReturn(passengerAcceptedPaymentDriverAvailable).when(driveRepository).save(passengerAcceptedPaymentDriverAvailable);

        driveService.paymentAccepted(passengersBankTransaction);

        verify(driveRepository, times(1)).findByOwner_User_EmailAndOwnerTransactionId(any(),  any());
        verify(driveRepository,times(1)).findAll();
        verify(driveRepository, times(4)).save(any());
        verify(userService, times(1)).getDrivers();
        verify(userService, times(1)).getDriver(driverAvailable.getUser().getEmail());

    }




    //payment accepted - pass payment accepted - payment answered -- payment not accepted
    @Test
    public void passengerAccepted_paymentRejected() throws TransactionIdDoesNotExistException, EmailNotFoundException, URISyntaxException, IOException, InterruptedException {
        passWithMoney.setTransactionId(2L);
        passWithMoney.setDebit(300.0);
        passengerCantAfford.setDebit(300.0);
        passWithMoney.setPayment(PaymentPassengerStatus.WAITING);
        passWithMoney.setContribution(DrivePassengerStatus.ACCEPTED);
        passengerCantAfford.setContribution(DrivePassengerStatus.ACCEPTED);
        passengerCantAfford.setPayment(PaymentPassengerStatus.REJECTED);
        passengerAcceptedPaymentPaymentDone.getPassengers().add(passengerCantAfford);
        passengerAcceptedPaymentWaiting.getPassengers().add(passengerCantAfford);

        List<Drive> drives = new ArrayList<>();
        drives.add(driveNoDriver_NoPassengers);
        drives.add(passengerAcceptedPaymentWaiting);

        String ownerEmail = drive_passengerPayment_OneAccepted_OtherWaiting.getOwner().getUser().getEmail();

        Mockito.doReturn(null).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(ownerEmail, passengerAcceptedPaymentWaiting.getOwnerTransactionId());
        Mockito.doReturn(drives).when(driveRepository).findAll();
        Mockito.doReturn(passengerAcceptedPaymentPaymentDone).when(driveRepository).save(passengerAcceptedPaymentWaiting);
        Mockito.doReturn(clientWithMoney).when(clientsRepository).findClientsAccountByUserEmail(passWithMoney.getPassengerEmail());
        Mockito.doReturn(clientWithMoney).when(userService).findClientsAccount(passWithMoney.getPassengerEmail());
        Mockito.doReturn(null).when(bankService).declineTransaction(any(), any());
        Mockito.doReturn(null).when(bankService).declineTransaction(any(), any());
        Mockito.doReturn(passengerAcceptedPaymentPaymentDone).when(driveRepository).save(passengerAcceptedPaymentPaymentDone);
        Mockito.doReturn(passengerAcceptedPaymentPaymentDone).when(driveRepository).save(passengerAcceptedPaymentPaymentDone);
        Mockito.doReturn(bankTransaction).when(bankService).requestOwnerPayment(any());
        Mockito.doReturn(passengerAcceptedPaymentPaymentDone).when(driveRepository).save(passengerAcceptedPaymentPaymentDone);


        driveService.paymentAccepted(passengersBankTransaction);

        verify(driveRepository, times(1)).findByOwner_User_EmailAndOwnerTransactionId(any(),  any());
        verify(driveRepository,times(1)).findAll();
        verify(driveRepository, times(4)).save(any());
        verify(bankService, times(2)).declineTransaction(any(), any());
        verify(bankService, times(1)).requestOwnerPayment(any());
        verify(userService, times(1)).findClientsAccount(passWithMoney.getPassengerEmail());
    }

    //test for payment decline : slucajevi
    //owner declined -->drive failed i tjt
    @Test
    public void paymentDeclined_OwnerPaymentDeclined() throws EmailNotFoundException, TransactionIdDoesNotExistException {
        driveNoDriver_NoPassengers.setOwnerTransactionId(rejectedBankTransaction.getId());

        Mockito.doReturn(driveNoDriver_NoPassengers).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        Mockito.doReturn(driveNoDriver_NoPassengers).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        Mockito.doReturn(failedDrive).when(driveRepository).save(driveNoDriver_NoPassengers);

        driveService.paymentDeclined(rejectedBankTransaction);

        verify(driveRepository, times(2)).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        verify(driveRepository, times(1)).save(any());


    }

    //drive not found exceptions
    //nezavisno
    @Test
    public void driveNotFoundExceptionThrown_DriveDoNotExistForDeclinedTransaction(){
        Mockito.doReturn(null).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(any(), any());

        assertThrows(NotFoundException.class, ()->{
            driveService.paymentAccepted(rejectedBankTransaction);
        });
    }

    //passenger declined -- payment not answered
    @Test
    public void declinedPayment_PaymentNotAnswered() throws EmailNotFoundException, TransactionIdDoesNotExistException {
        rejectedBankTransaction.setSender(passWithMoney.getPassengerEmail());
        passWithMoney.setTransactionId(rejectedBankTransaction.getId());
        passWithMoney.setDebit(300.0);
        passengerCantAfford.setDebit(300.0);
        passWithMoney.setPayment(PaymentPassengerStatus.REJECTED);
        passWithMoney.setContribution(DrivePassengerStatus.ACCEPTED);
        passengerCantAfford.setContribution(DrivePassengerStatus.ACCEPTED);
        passengerCantAfford.setPayment(PaymentPassengerStatus.WAITING);
        Set<Passenger> passengers = new HashSet<>();
        passengers.add(passWithMoney);
        passengers.add(passengerCantAfford);
        driveNoDriver_WithPassengers.setPassengers(passengers);
        List<Drive> drives = new ArrayList<>();
        drives.add(driveNoDriver_WithPassengers);

        Mockito.doReturn(null).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        Mockito.doReturn(drives).when(driveRepository).findAll();
        Mockito.doReturn(driveNoDriver_WithPassengers).when(driveRepository).save(driveNoDriver_WithPassengers);
        Mockito.doReturn(driveNoDriver_WithPassengers).when(driveRepository).save(driveNoDriver_WithPassengers);

        driveService.paymentDeclined(rejectedBankTransaction);

        verify(driveRepository, times(1)).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        verify(driveRepository, times(2)).save(any());
        verify(driveRepository, times(1)).findAll();

    }

    //payment answered
    @Test
    public void paymentDeclines_PassengerDeclinePayment_PaymentAnswered() throws TransactionIdDoesNotExistException, EmailNotFoundException {
        rejectedBankTransaction.setSender(passWithMoney.getPassengerEmail());
        passWithMoney.setTransactionId(rejectedBankTransaction.getId());
        passWithMoney.setDebit(300.0);
        passengerCantAfford.setDebit(300.0);
        passWithMoney.setPayment(PaymentPassengerStatus.REJECTED);
        passWithMoney.setContribution(DrivePassengerStatus.ACCEPTED);
        passengerCantAfford.setContribution(DrivePassengerStatus.ACCEPTED);
        passengerCantAfford.setPayment(PaymentPassengerStatus.NOT_PAYING);
        Set<Passenger> passengers = new HashSet<>();
        passengers.add(passWithMoney);
        passengers.add(passengerCantAfford);
        driveNoDriver_WithPassengers.setPassengers(passengers);
        List<Drive> drives = new ArrayList<>();
        drives.add(driveNoDriver_WithPassengers);

        Mockito.doReturn(null).when(driveRepository).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        Mockito.doReturn(drives).when(driveRepository).findAll();
        Mockito.doReturn(null).when(bankService).declineTransaction(any(), any());
        Mockito.doReturn(null).when(bankService).declineTransaction(any(), any());
        Mockito.doReturn(driveNoDriver_WithPassengers).when(driveRepository).save(driveNoDriver_WithPassengers);
        Mockito.doReturn(driveNoDriver_WithPassengers).when(driveRepository).save(driveNoDriver_WithPassengers);
        Mockito.doReturn(bankTransaction).when(bankService).requestOwnerPayment(driveNoDriver_WithPassengers);
        Mockito.doReturn(driveNoDriver_WithPassengers).when(driveRepository).save(driveNoDriver_WithPassengers);
        Mockito.doReturn(driveNoDriver_WithPassengers).when(driveRepository).save(driveNoDriver_WithPassengers);


        driveService.paymentDeclined(rejectedBankTransaction);

        verify(driveRepository, times(1)).findByOwner_User_EmailAndOwnerTransactionId(any(), any());
        verify(driveRepository, times(1)).findAll();
        verify(bankService, times(1)).declineTransaction(any(), any());
        verify(bankService, times(1)).requestOwnerPayment(driveNoDriver_WithPassengers);
        verify(driveRepository, times(4)).save(any());



    }
}
