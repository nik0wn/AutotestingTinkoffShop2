import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AccountServerTest {
    private void deleteUserById(Long id){
        var pathDeleteUserById = "/deleteUserById?id=" + id;
        var responseDeleteUserById = TestUtils.callDelete(pathDeleteUserById);
        assertThat(responseDeleteUserById.extract().statusCode()).as("statusCode").isEqualTo(200);
    }

    @Test
    public void checkReplenishmentBalance(){
        var user = "Anton";
        var phone = "89128700008";
        var balance = 100;
        var pathAddUsers = "/addUsers?name=" + user + "&phone=" +phone;
        var responseAddUsers = TestUtils.callPost(pathAddUsers);
        var userId = Long.valueOf((Integer) responseAddUsers.extract().body().path("id"));
        var userPhone = responseAddUsers.extract().body().path("phone");
        assertThat(responseAddUsers.extract().statusCode()).as("statusCode").isEqualTo(200);
        assertThat(userPhone).isEqualTo(phone);

        var pathReplenishmentBalance = "/replenishmentBalance?phone=" + phone + "&balance=" + balance;
        var responseReplenishmentBalance = TestUtils.callPut(pathReplenishmentBalance);
        var accountUserID = Long.valueOf((Integer) responseReplenishmentBalance.extract().body().path("userId"));
        assertThat(responseReplenishmentBalance.extract().statusCode()).as("statusCode").isEqualTo(200);
        assertThat(accountUserID).isEqualTo(userId);

        deleteUserById(userId);
   }
    @Test
    public void errorReplenishmentBalance(){
        var phone = "89128700008";
        var balance = 100;
        var pathReplenishmentBalance = "/replenishmentBalance?phone=" + phone + "&balance=" + balance;
        var response = TestUtils.callPut(pathReplenishmentBalance).assertThat().statusCode(404);
        var message = response.extract().body().path("message").toString();
        var messExpect = "Пользователя с телефоном '" + phone +"' не существует!";
        Assertions.assertThat(message).as("message").isEqualTo(messExpect);
    }
}
