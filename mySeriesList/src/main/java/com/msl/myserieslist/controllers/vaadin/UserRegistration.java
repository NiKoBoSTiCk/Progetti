package com.msl.myserieslist.controllers.vaadin;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.msl.myserieslist.entities.User;
import com.msl.myserieslist.services.AccountingService;
import com.msl.myserieslist.support.exceptions.MailUserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;


@Route("userRegistration")
@Tag("user-registration")
@JsModule("./src/user-registration.js")
public class UserRegistration extends PolymerTemplate<UserRegistration.UserRegistrationModel> {
    @Autowired
    private AccountingService accountingService;

    @Id("emailTextField")
    private TextField emailTextField;
    @Id("lastNameTextField")
    private TextField lastNameTextField;
    @Id("firstNameTextField")
    private TextField firstNameTextField;
    @Id("submitButton")
    private Button submitButton;
    @Id("resultLabel")
    private Label resultLabel;


    public UserRegistration() {
        Binder<User> binder = new Binder<>(User.class);
        binder.setBean(new User());
        binder.forField(firstNameTextField).withValidator(name -> name.length() >= 3, "Name must contain at least three characters!").bind(User::getFirstName, User::setFirstName);
        binder.forField(lastNameTextField).bind(User::getLastName, User::setLastName);
        binder.forField(emailTextField).withValidator(new EmailValidator("Incorrect email address!")).bind(User::getEmail, User::setEmail);
        submitButton.addClickListener(buttonClickEvent -> {
            if (binder.validate().isOk()) {
                try {
                    accountingService.registerUser(binder.getBean());
                    emailTextField.setValue("");
                    lastNameTextField.setValue("");
                    firstNameTextField.setValue("");
                    resultLabel.setText("Done!");
                } catch (MailUserAlreadyExistsException e) {
                    resultLabel.setText("Error, email address already exist!");
                }
            }
        });
    }

    public interface UserRegistrationModel extends TemplateModel {}
}
