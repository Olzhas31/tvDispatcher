package com.example.tvDispatcher.config;

import com.example.tvDispatcher.entity.Gender;
import org.springframework.stereotype.Component;

@Component
public class Data {

    private final String[] roles = {
            "ADMIN",
            "DISPATCHER",
            "JURGIZYSHI",
            "OPERATOR",
            "TILSHI",
            "MANAGER",
            "USER"
    };

    // {name, description}
    private final String[][] departments = {
            {"Диспетчерлік департамент", "Диспетчерлік департамент (диспетчерлер қарайды)"},
            {"Тасымалдау департаменті", "Тасымалдау департаменті (жүргізушілер қарайды)"},
            {"Операторлар департаменті", "Операторлар департаменті (операиорлар қарайды)"},
            {"Тілшілер департаменті", "Тілшілер департаменті (тілшілер қарайды)"}
    };

    // {email, role, department_name, name, surname, middle_name, gender, phone_number, about_information}
    private final String[][] users = {
            {"aidos_manager@gmail.com", roles[5], departments[0][0], "Айдос", "Рысқұлбеков", "Омарұлы", Gender.ER.name(), "+7 777 000 12 12", ""},
            {"samat_dispatcher@gmail.com", roles[1], departments[0][0], "Самат", "Маратов", "Әлібекұлы", Gender.ER.name(), "+7 701 111 11 11", ""},
            {"lazzat_dispatcher@gmail.com", roles[1], departments[0][0], "Ләззат", "Сәрсенбаева", "Сағидоллақызы", Gender.AYEL.name(), "+7 701 111 22 22", ""},
            {"saule_dispatcher@gmail.com", roles[1], departments[0][0], "Сәуле", "Есдәулетова", "Ғабитқызы", Gender.AYEL.name(), "+7 701 111 33 33", ""},
            {"aidyn_manager@gmail.com", roles[5], departments[1][0], "Айдын", "Қажымұқан", "Зейноллаұлы", Gender.ER.name(), "+7 777 000 22 22", ""},
            {"askhat_jurgizushi@gmail.com", roles[2], departments[1][0], "Асхат", "Сырымбет", "Сәкенұлы", Gender.ER.name(), "+7 707 222 22 22", ""},
            {"bagdat_jurgizushi@gmail.com", roles[2], departments[1][0], "Бағдат", "Жұбалай", "Аманович", Gender.ER.name(), "+7 707 222 33 33", ""},
            {"aiqyn_jurgizushi@gmail.com", roles[2], departments[1][0], "Айқын", "Теңізбаев", "Мақсатұлы", Gender.ER.name(), "+7 707 222 44 44", ""},
            {"aibek_jurgizushi@gmail.com", roles[2], departments[1][0], "Айбек", "Ержігіт", "Жарқынұлы", Gender.ER.name(), "+7 707 222 55 55", ""},
            {"nurken_jurgizushi@gmail.com", roles[2], departments[1][0], "Нұркен", "Қали", "Мұстафаұлы", Gender.ER.name(), "+7 707 222 66 66", ""},
            {"damir_manager@gmail.com", roles[5], departments[2][0], "Дамир", "Тағаев", "Айдосұлы", Gender.ER.name(), "+7 777 000 32 32", ""},
            {"dias_operator@gmail.com", roles[3], departments[2][0], "Дияс", "Қайратов", "Серікұлы", Gender.ER.name(), "+7 771 333 88 88", ""},
            {"serikbol_operator@gmail.com", roles[3], departments[2][0], "Серікбол", "Доссай", "Тохтарұлы", Gender.ER.name(), "+7 771 333 99 99", ""},
            {"asanali_operator@gmail.com", roles[3], departments[2][0], "Асанәлі", "Көбей", "Дарханұлы", Gender.ER.name(), "+7 771 333 77 77", ""},
            {"miras_operator@gmail.com", roles[3], departments[2][0], "Мирас", "Қырғызбай", "Қуанышұлы", Gender.ER.name(), "+7 771 333 66 66", ""},
            {"didar_operator@gmail.com", roles[3], departments[2][0], "Дидар", "Есхатов", "Илиярұлы", Gender.ER.name(), "+7 771 333 55 55", ""},
            {"aqmarjan_manager@gmail.com", roles[5], departments[3][0], "Ақмаржан", "Ерсін", "Бекжанқызы", Gender.AYEL.name(), "+7 777 000 42 42", ""},
            {"aisulu_tilshi@gmail.com", roles[4], departments[3][0], "Айсұлу", "Нұрдаулетова", "Ерболатовна", Gender.AYEL.name(), "+7 747 444 44 44", ""},
            {"korkem_tilshi@gmail.com", roles[4], departments[3][0], "Көркем", "Наурызбай", "Жанболатқызы", Gender.AYEL.name(), "+7 747 444 11 11", ""},
            {"sezim_tilshi@gmail.com", roles[4], departments[3][0], "Сезім", "Қасенова", "Мәлікқызы", Gender.AYEL.name(), "+7 747 444 00 00", ""},
            {"aidar_tilshi@gmail.com", roles[4], departments[3][0], "Айдар", "Шыныбеков", "Қасымұлы", Gender.ER.name(), "+7 747 444 22 22", ""},
            {"farkhat_tilshi@gmail.com", roles[4], departments[3][0], "Фархат", "Тынышбай", "Жомарұлы", Gender.ER.name(), "+7 747 444 33 33", ""},
    };

    public String[][] getDepartments(){
        return departments;
    }

    public String[][] getUsers() {
        return users;
    }

    public String[] getRoles() {
        return roles;
    }
}