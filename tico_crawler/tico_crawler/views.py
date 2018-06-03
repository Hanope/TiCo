from django.views.decorators.csrf import csrf_exempt
from . import crawller
from django.http import JsonResponse
import json

@csrf_exempt
def timetable(request):
    user_id = request.GET.get('user_id')
    user_password = request.GET.get('user_password')
    data = crawller.crawlling(user_id, user_password)
    json_result = json.loads(data)

    return JsonResponse(json_result)