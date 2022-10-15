import os

from slack_sdk import WebClient


def main():
    androidSlackBotToken = os.environ['ANDROID_SLACK_BOT_TOKEN']
    branchName = os.environ['CURRENT_BRANCH']
    androidSlackChannelKey = "C045WAKTQ6Q"
    client = WebClient(token=androidSlackBotToken)
    response = client.files_upload_v2(
        file="app/build/outputs/apk/debug/app-debug.apk", 
        title="Android apk build",
        channel=androidSlackChannelKey,
        initial_comment=":white_check_mark: ${{ github.ref_name }} branch was successfully build! :white_check_mark:",
    )
    response.get("file")


if __name__ == "__main__":
    main()
