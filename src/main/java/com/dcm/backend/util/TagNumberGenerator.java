package com.dcm.backend.util;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.dcm.backend.entity.JewelleryItems;
import com.dcm.backend.repository.EmployeeRepository;
import com.dcm.backend.repository.JewelleryItemsRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TagNumberGenerator {


    private final JewelleryItemsRepository itemRepository;

    public Long generateTagNumber(String metalType) {

        if (metalType.toUpperCase().equals("GOLD")) {
        	Optional<JewelleryItems> item =
                    itemRepository.findTopByMainProductOrderByIdDesc(metalType);
        	if(item.isEmpty())
        	{
        		return 140000L;
        	}
        	Long tagNumber = item.get().getTagNumber();

            Long nextNumber =tagNumber+ 1;

            return nextNumber;

        }
        else if(metalType.toUpperCase().equals("SILVER"))
        {
        	Optional<JewelleryItems> item =
                    itemRepository.findTopByMainProductOrderByIdDesc(metalType);
        	if(item.isEmpty())
        	{
        		return 500000L;
        	}
        	Long tagNumber = item.get().getTagNumber();

            Long nextNumber =tagNumber+ 1;

            return nextNumber;
        }
		return 0L;
    }

}