#include <chrono>
#include <iostream>
#include <opencv2/opencv.hpp>

std::vector<cv::Point2f> control_points;

void mouse_handler(int event, int x, int y, int flags, void *userdata) 
{
    if (event == cv::EVENT_LBUTTONDOWN && control_points.size() < 4) 
    {
        std::cout << "Left button of the mouse is clicked - position (" << x << ", "
        << y << ")" << '\n';
        control_points.emplace_back(x, y);
    }     
}

void naive_bezier(const std::vector<cv::Point2f> &points, cv::Mat &window) 
{
    auto &p_0 = points[0];
    auto &p_1 = points[1];
    auto &p_2 = points[2];
    auto &p_3 = points[3];

    for (double t = 0.0; t <= 1.0; t += 0.001) 
    {
        auto point = std::pow(1 - t, 3) * p_0 + 3 * t * std::pow(1 - t, 2) * p_1 +
                 3 * std::pow(t, 2) * (1 - t) * p_2 + std::pow(t, 3) * p_3;

        window.at<cv::Vec3b>(point.y, point.x)[2] = 255;
    }
}

cv::Point2f recursive_bezier(const std::vector<cv::Point2f> &control_points, float t) 
{
   auto ans = std::pow(t,3) * control_points[0] + t * t * (1-t) * 3 * (control_points[1]) + t * (1-t) * (1-t) * 3 * (control_points[2]) + std::pow(1-t,3) * control_points[3]; 
    return ans;

}

void bezier(const std::vector<cv::Point2f> &control_points, cv::Mat &window) 
{
    // TODO: Iterate through all t = 0 to t = 1 with small steps, and call de Casteljau's 
    // recursive Bezier algorithm.
    for(double t = 0; t <= 1; t += 0.001){
        auto point = recursive_bezier(control_points,t);
        auto y1 = std::floor(point.y + 0.5),y2 = std::floor(point.y - 0.5),
            x1 = std::floor(point.x + 0.5),x2 = std::floor(point.x - 0.5);
        float p1 = 1- std::sqrt(std::pow(y1-point.y,2)+std::pow(x1-point.x,2)),
            p2 = 1- std::sqrt(std::pow(y1-point.y,2)+std::pow(x2-point.x,2)),
            p3 = 1- std::sqrt(std::pow(y2-point.y,2)+std::pow(x1-point.x,2)),
            p4 = 1- std::sqrt(std::pow(y2-point.y,2)+std::pow(x2-point.x,2));

        window.at<cv::Vec3b>(point.y,point.x)[1] = 255;
        window.at<cv::Vec3b>(y1,x1)[1] = std::max((float)window.at<cv::Vec3b>(y1,x1)[1],255 * p1);
        window.at<cv::Vec3b>(y1,x2)[1] = std::max((float)window.at<cv::Vec3b>(y1,x2)[1],255 * p2);
        window.at<cv::Vec3b>(y2,x1)[1] = std::max((float)window.at<cv::Vec3b>(y2,x1)[1],255 * p3);
        window.at<cv::Vec3b>(y2,x2)[1] = std::max((float)window.at<cv::Vec3b>(y2,x1)[1],255 * p4);
        
    }
}

int main() 
{
    cv::Mat window = cv::Mat(700, 700, CV_8UC3, cv::Scalar(0));
    cv::cvtColor(window, window, cv::COLOR_BGR2RGB);
    cv::namedWindow("Bezier Curve", cv::WINDOW_AUTOSIZE);

    cv::setMouseCallback("Bezier Curve", mouse_handler, nullptr);

    int key = -1;
    while (key != 27) 
    {
        for (auto &point : control_points) 
        {
            cv::circle(window, point, 3, {255, 255, 255}, 3);
        }

        if (control_points.size() == 4) 
        {
            naive_bezier(control_points, window);
            bezier(control_points, window);

            cv::imshow("Bezier Curve", window);
            cv::imwrite("my_bezier_curve.png", window);
            key = cv::waitKey(0);

            return 0;
        }

        cv::imshow("Bezier Curve", window);
        key = cv::waitKey(20);
    }

return 0;
}
