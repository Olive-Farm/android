import os

from slack_sdk import WebClient


def main():
    androidSlackBotToken = os.environ['ANDROID_SLACK_BOT_TOKEN']
    androidSlackChannelKey = "C045MGC472A"
    client = WebClient(token=androidSlackBotToken)
    response = client.files_upload_v2(
        file="app/build/outputs/apk/debug/app-debug.apk",
        title="Android apk build",
        channel=androidSlackChannelKey,
        initial_comment="Apk was successfully build! :white_check_mark:",
#         thread_ts="1665807214.774699"
    )
    result = response.get("file")
    print(result['url_private_download'])


if __name__ == "__main__":
    main()
