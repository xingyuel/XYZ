package com.xyz.misc;

import com.xyz.dto.ContactDTO;
import com.xyz.entities.Contact;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.beanutils.BeanUtils;

public class Utils {

    // from ContactDTO to Contact
    public static Optional<Contact> toEntity(ContactDTO dto) {
        Contact entity = new Contact();
        try {
            BeanUtils.copyProperties(entity, dto);
        } catch (IllegalAccessException | InvocationTargetException | RuntimeException ie) {
            return Optional.empty();
        }

        return Optional.ofNullable(entity);
    }

    // from Contact to ContactDTO
    public static Optional<ContactDTO> toDto(Contact entity) {
        ContactDTO dto = new ContactDTO();
        try {
            BeanUtils.copyProperties(dto, entity);
        } catch (IllegalAccessException | InvocationTargetException | RuntimeException ie) {
            return Optional.empty();
        }

        return Optional.ofNullable(dto);
    }

    // to ContactDTO list
    public static List<ContactDTO> toDtoList(List<Contact> entities) {
        List<ContactDTO> dtos = new ArrayList<>(entities.size());

        entities.forEach(ent -> {
            Optional<ContactDTO> opt = toDto(ent);
            opt.ifPresent(dto -> dtos.add(dto));
        });

        return dtos;
    }

    // this is called within the transaction
    public static void updateEntity(Contact dest, Contact src) {
        Long id = dest.getId();
        try {
            BeanUtils.copyProperties(dest, src);
            dest.setId(id);
        } catch (IllegalAccessException | InvocationTargetException | RuntimeException ie) {
        }
    }

    /*
        When multiple Contact records are returned, this method is used to return that one
        with the most similarity to the first parameter.
     */
    public static Contact findMostSimilar(Contact updated, List<Contact> origs) {
        if (origs == null || origs.isEmpty()) {
            throw new RuntimeException("Blank origs in findMostSimilar(Contact updated, List<Contact> origs)!");
        }

        if (origs.size() == 1) {
            return origs.get(0);
        }

        Map<Integer, Contact> map = new HashMap<>(origs.size());
        origs.forEach(cont -> {
            Integer similarity = evalSimilarity(updated, cont);
            map.put(similarity, cont);      // suppose there are multiple records with
            // the same key, the last one will be in Map
        });

        /*  Maybe a better way:
    Create an class, ContactWithSimilarity, containing similarity and a Contact. 

    List<ContactWithSimilarity> list = ....
    Map<Integer, ContactWithSimilarity> map = list.stream().collect(groupingBy(ContactWithSimilarity::getSimilarity));
         */
        Set<Integer> similarities = map.keySet();
        Optional<Integer> mostSimilar = similarities.stream().reduce(Integer::max);

        return map.get(mostSimilar.get());      // guarantee the Optional is not empty
    }

    /*
        If N fields are the same, the return value will be N.
    */
    private static Integer evalSimilarity(Contact cont1, Contact cont2) {
        Integer retVal = 0;
        if (Objects.equals(cont1.getFirstName(), cont2.getFirstName())) {
            retVal++;
        }
        if (Objects.equals(cont1.getLastName(), cont2.getLastName())) {
            retVal++;
        }
        if (Objects.equals(cont1.getEmail(), cont2.getEmail())) {
            retVal++;
        }
        if (Objects.equals(cont1.getAddress1(), cont2.getAddress1())) {
            retVal++;
        }
        if (Objects.equals(cont1.getAddress2(), cont2.getAddress2())) {
            retVal++;
        }
        if (Objects.equals(cont1.getCity(), cont2.getCity())) {
            retVal++;
        }
        if (Objects.equals(cont1.getProvince(), cont2.getProvince())) {
            retVal++;
        }
        if (Objects.equals(cont1.getZipCode(), cont2.getZipCode())) {
            retVal++;
        }
        if (Objects.equals(cont1.getCountry(), cont2.getCountry())) {
            retVal++;
        }
        if (Objects.equals(cont1.getPhoneNumber(), cont2.getPhoneNumber())) {
            retVal++;
        }

        return retVal;
    }
}
