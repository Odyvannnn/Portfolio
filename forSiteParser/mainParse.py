import requests
import re
from bs4 import BeautifulSoup


class mainSiteParse:
    def get_page(self):
        sectors = []
        finish = []
        final_num = []
        costs = []
        count = 0
        url = 'https://www.spb-hifi.ru/shop'
        response = requests.get(url)
        web_page = response.text
        soup = BeautifulSoup(web_page, "html.parser")
        elms = soup.find_all('a', {'class': 'product_link'})
        for i in elms:
            sectors.append(i.attrs["href"])
            max_page = self.get_max(finish, sectors[count])
            num_page = self.get_num(final_num, max_page[count])
            info_pages = self.get_url(costs, num_page[count], sectors[count])
            count += 1
        print(sectors)
        print(num_page)
        return sectors

    def get_max(self, finish, sectors):

        url = 'https://www.spb-hifi.ru{sectors}'.format(
            sectors=sectors
        )
        response = requests.get(url)
        web_page = response.text
        soup = BeautifulSoup(web_page, "html.parser")
        elms = soup.find('a', {'title': 'В конец'})
        try:
            go = elms.attrs["href"]
        except AttributeError:
            go = sectors+'?start=0'
        finish.append(go)
        return finish

    def get_num(self, final_num, max_page):
        nums = re.findall(r'\d+', max_page)
        nums = [int(i) for i in nums]
        final_num.append(nums[0])
        return final_num

    def get_url(self, costs, num_page, sectors):
        counter = 0
        while counter <= num_page:
            url = '{domain}{sectors}?start={num_page}.htm'.format(
                domain='https://www.spb-hifi.ru',
                sectors=sectors,
                num_page=num_page
            )
            response = requests.get(url)
            web_page = response.text
            soup = BeautifulSoup(web_page, "html.parser")
            elms2 = soup.find_all("td", {"class": "info"})
            for container in elms2:
                cost = container.find_all('div', {'class': 'jshop_price'})[0].text[7:-3]
                costs.append(cost)
            counter += 12
        print(costs)
        return costs


if __name__ == '__main__':
    arg = mainSiteParse()
    sec = arg.get_page()

