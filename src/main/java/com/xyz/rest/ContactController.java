package com.xyz.rest;

import com.xyz.dto.ContactDTO;
import com.xyz.entities.Contact;
import com.xyz.misc.Action;
import static com.xyz.misc.Action.Delete;
import static com.xyz.misc.Action.Save;
import static com.xyz.misc.Action.Update;
import com.xyz.misc.Utils;
import com.xyz.service.ContactService;
import com.xyz.service.ZipcodeApiService;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(
        value = "Contact/v1",
        produces = {"application/json"},
        consumes = {"*/*"})
public class ContactController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactController.class);

    private final ContactService service;

    private final ZipcodeApiService zipService;

    @Autowired
    public ContactController(ContactService service, ZipcodeApiService zipService) {
        this.service = service;
        this.zipService = zipService;
    }

    // SEARCHING
    @RequestMapping(value = "/byFirstName", method = RequestMethod.GET)
    public List<ContactDTO> findByFirstName(@RequestParam(required = true) String firstName) {
        LOGGER.info("input: " + firstName);

        return Utils.toDtoList(service.findByFirstName(firstName));
    }

    @RequestMapping(value = "/byLastName", method = RequestMethod.GET)
    public List<ContactDTO> findByLastName(@RequestParam(required = true) String lastName) {
        LOGGER.info("input: " + lastName);

        return Utils.toDtoList(service.findByLastName(lastName));
    }

    @RequestMapping(value = "/byZipCode", method = RequestMethod.GET)
    public List<ContactDTO> findByZipCode(@RequestParam(required = true) Integer zipCode) {
        LOGGER.info("input: " + zipCode);

        return Utils.toDtoList(service.findByZipCode(zipCode));
    }

    // SAVE
    @PostMapping
    public void createContact(@Valid @RequestBody ContactDTO dto) {
        execute(dto, Action.Save);
    }

    // UPDATE
    @RequestMapping(method = RequestMethod.PUT)
    public void updateContact(@Valid @RequestBody ContactDTO dto) {
        execute(dto, Action.Update);
    }

    // DELETE
    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteContact(@Valid @RequestBody ContactDTO dto) {
        execute(dto, Action.Delete);
    }

    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public void deleteById(@RequestParam(required = true) Long id) {
        LOGGER.info("id = " + id);
    }

    // Enable zipcodeApi REST call. The default is to use Chicago's zipcodes.
    @RequestMapping(value = "/enableZipcodeApi", method = RequestMethod.PUT)
    public void enableZipcodeApi(@RequestParam(required = true) Boolean flag) {
        LOGGER.info("enalbeZipcodeApi: " + flag);

        zipService.setCallRest(flag);
    }

    private void execute(ContactDTO dto, Action act) {
        LOGGER.info("input: " + dto.toString());
        Optional<Contact> opt = Utils.toEntity(dto);
        opt.ifPresent(entity -> {
            zipService.validate(entity.getCity(), entity.getProvince(), entity.getZipCode());

            if (null != act) {
                switch (act) {
                    case Save:
                        service.save(entity);
                        break;
                    case Update:
                        service.update(entity);
                        break;
                    case Delete:
                        service.delete(entity);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
