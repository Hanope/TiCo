from selenium import webdriver
from selenium.webdriver.support.ui import Select
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
from selenium.common.exceptions import NoAlertPresentException
from selenium.webdriver.common.by import By
from bs4 import BeautifulSoup
import json
import os

def getDriver(url):
    options = webdriver.ChromeOptions()
    options.add_argument('headless')
    options.add_argument('disable-gpu')
    chrome_path = os.path.join(os.path.dirname(os.path.realpath(__file__)), 'chromedriver')
    driver = webdriver.Chrome(chrome_path, chrome_options=options)
    driver.get(url)

    return driver

def createJsonResult(result, data):
    json_data = {}
    json_data["result"] = result
    json_data["message"] = data

    return json.dumps(json_data, indent=4, ensure_ascii=False)

def getTimetable(tables):
    monday = {"timetables": []}
    tuesday = {"timetables": []}
    wednesday = {"timetables": []}
    thursday = {"timetables": []}
    friday = {"timetables": []}
    day = [monday, tuesday, wednesday, thursday, friday]
    schedule = {};

    # 시간표별 tr loop
    for tr in tables:
        th = tr.find('th')
        tds = tr.find_all('td')
        time_info = th.contents

        time = time_info[0];
        hours = time_info[2];

        for idx, td in enumerate(tds):
            course_info = td.contents

            if len(course_info) > 0:
                course_name = course_info[0]
                course_code = course_info[2]
                course_location = course_info[4]

                tables = {
                    "time": time,
                    "hours": hours,
                    "course_name": course_name,
                    "course_code": course_code,
                    "course_location": course_location
                }

                day[idx]["timetables"].append(tables)

    schedule["monday"] = monday
    schedule["tuesday"] = tuesday
    schedule["wednesday"] = wednesday
    schedule["thursday"] = thursday
    schedule["friday"] = friday

    return schedule



def getSchedule(driver, id, password):
    # id/password 입력
    driver.find_element_by_name('user.usr_id').send_keys(id)
    driver.find_element_by_name('user.passwd').send_keys(password)

    # click login button
    driver.find_element_by_id('loginBtn').click()

    timeout = 10

    try:
        alert = driver.switch_to_alert()
        return createJsonResult('failed', alert.text)
    except NoAlertPresentException:
        try:
            element_present = EC.presence_of_element_located((By.ID, 'search_open_yr_trm'))
            WebDriverWait(driver, timeout).until(element_present)

            # select semester
            semester = Select(driver.find_element_by_id('search_open_yr_trm'))
            semester.select_by_index(1)

            # click search btn
            driver.find_element_by_xpath('//p[contains(@class, "btn")]').click()

            # selenium BeautifulSoup으로 변환
            html = driver.page_source
            soup = BeautifulSoup(html, 'lxml')
            time_table = soup.findAll('table', {'class': 'cour_table'})
            tables = time_table[0].tbody.find_all('tr')

            return createJsonResult('success', getTimetable(tables))
        except TimeoutException:
            return createJsonResult('failed', 'Timeout Error')
    finally:
        driver.quit()


def crawlling(user_id, user_password):
    url = 'http://my.knu.ac.kr/stpo/stpo/cour/lectReqEnq/list.action'
    driver = getDriver(url)

    return getSchedule(driver, user_id, user_password)