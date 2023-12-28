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
    static final String SERVER_ERROR_MESSAGE = "Internal Server Error !";
    static final int SERVER_ERROR_CODE = 500;

    public List<YearsResDto> getAllYearsByCategory(long categoryId) {
        try {
            List<Years> yearsList = yearsRepository.findByCategory(categoryId);
            return new ArrayList<>(yearsList.stream().map(YearsResDto::new).toList());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public Optional<Years> getYearByCategoryAndYear(long categoryId, int year) {
        try {
            return yearsRepository.getYearByCategoryAndYear(categoryId, year);
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public YearsResDto addNewYear(YearsReqDto yearsReqDto, long categoryId) {
        try {
            Optional<Years> optYears = yearsRepository.findByCategoryAndYear(categoryId, yearsReqDto.getYear());
            if (optYears.isPresent()) {
                throw new CustomException("Year already exist for this category !", 400);
            }
            Years years = new Years(yearsReqDto, categoryId);
            Years savedYears = yearsRepository.save(years);
            return new YearsResDto(savedYears);
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage(), ex.getStatus());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public YearsResDto editYear(YearsReqDto yearsReqDto, long categoryId, long yearId) {
        try {
            Optional<Years> optionalYears = yearsRepository.findById(yearId);
            if (optionalYears.isEmpty()) {
                throw new CustomException("Year not found !", 404);
            }

            Optional<Years> optYear = yearsRepository.findByCategoryAndYearNotId(categoryId, yearsReqDto.getYear(), yearId);
            if (optYear.isPresent()) {
                throw new CustomException("Year already exist for this category !", 400);
            }

            Years year = optionalYears.get();
            year.setYear(yearsReqDto.getYear());
            year.setAmountLimit(yearsReqDto.getAmountLimit());
            year.setItemLimit(yearsReqDto.getItemLimit());
            Years savedYear = yearsRepository.save(year);
            return new YearsResDto(savedYear);
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage(), ex.getStatus());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

}
