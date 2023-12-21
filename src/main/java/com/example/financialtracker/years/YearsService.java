package com.example.financialtracker.years;

import com.example.financialtracker.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class YearsService {
    private final YearsRepository yearsRepository;

    public List<YearsResDto> getAllYearsByCategory(long categoryId) {
        List<Years> yearsList = yearsRepository.findByCategory(categoryId);
        return new ArrayList<>(yearsList.stream().map(YearsResDto::new).toList());
    }

    public YearsResDto getSingleYear(long yearId) {
        Optional<Years> optYear = yearsRepository.findById(yearId);
        if (optYear.isEmpty()) {
            throw new CustomException("Year not found !", 404);
        }
        Years year = optYear.get();
        return new YearsResDto(year);
    }

    public YearsResDto addNewYear(YearsReqDto yearsReqDto, long categoryId){
        Optional<Years> optYears = yearsRepository.findByCategoryAndYear(categoryId, yearsReqDto.getYear());
        if(optYears.isPresent()){
            throw new CustomException("Year already exist for this category !", 400);
        }
        Years years = new Years(yearsReqDto, categoryId);
        Years savedYears = yearsRepository.save(years);
        return new YearsResDto(savedYears);
    }

    public YearsResDto editYear(YearsReqDto yearsReqDto, long categoryId, long yearId){

        Optional<Years> optionalYears = yearsRepository.findById(yearId);
        if(optionalYears.isEmpty()){
            throw new CustomException("Year not found !", 404);
        }

        Optional<Years> optYear = yearsRepository.findByCategoryAndYearNotId(categoryId, yearsReqDto.getYear(), yearId);
        if(optYear.isPresent()){
            throw new CustomException("Year already exist for this category !", 400);
        }

        Years year = optionalYears.get();
        year.setYear(yearsReqDto.getYear());
        year.setAmountLimit(yearsReqDto.getAmountLimit());
        year.setItemLimit(yearsReqDto.getItemLimit());
        Years savedYear = yearsRepository.save(year);
        return new YearsResDto(savedYear);
    }

}
